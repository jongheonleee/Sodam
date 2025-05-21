package sodam.backend.payment.learning.redis

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import mu.KotlinLogging
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.geo.Point
import org.springframework.data.redis.connection.DataType
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.test.context.ActiveProfiles
import java.util.*
import kotlin.NoSuchElementException
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration
import sodam.backend.payment.test.config.WithRedisContainer
import sodam.backend.payment.test.config.all


/**
 * - Redis 학습 테스트 코드
 */
private val logger = KotlinLogging.logger {}

@SpringBootTest
@ActiveProfiles("test")
class RedisLearningTest(
    private val template: ReactiveRedisTemplate<Any, Any>
) : WithRedisContainer, StringSpec({
    val KEY = "key"

    afterTest {
        template.delete(KEY).awaitSingle()
    }

    // Reactive Redis의 경우 코루틴에서 정상 동작하게 만들려면 항상 .awaitSingle() 호출해야함
    "hello reactive redis" {
        val ops = template.opsForValue()

        shouldThrow<NoSuchElementException> {
            ops.get(KEY).awaitSingle()
        }

        ops.set(KEY, "bla bla").awaitSingle()
        ops.get(KEY).awaitSingle() shouldBe "bla bla"

        template.expire(KEY, 3.seconds.toJavaDuration()).awaitSingle()
        delay(5.seconds)

        shouldThrow<NoSuchElementException> {
            ops.get(KEY).awaitSingle()
        }
    }
    /**
     *  Redis는 문자열, 해시, 리스트, 셋, 정렬된 셋, 비트맵, 하이퍼로그로그, 스트림 등 다양한 고성능 자료구조를 제공
     */
    "Linked List" {// Stack, Queue -> 대기열 관리, LRU 캐시
        val ops = template.opsForList()
        ops.rightPushAll(KEY,2, 3, 4, 5).awaitSingle()

        template.type(KEY).awaitSingle() shouldBe DataType.LIST

        ops.size(KEY).awaitSingle() shouldBe 4

//        val end = ops.size(KEY).awaitSingle() - 1
//        for (i in 0 .. end) {
//            ops.index(KEY, i).awaitSingle().let {
//                logger.debug { "$i: $it" }
//            }
//        }

//        ops.range(KEY, 0, -1).asFlow().collect { logger.debug { it } }
//        ops.range(KEY, 0, -1).toStream().forEach { logger.debug { it } }
//        ops.range(KEY, 0, -1).asFlow().toList() shouldBe listOf(2, 3, 4, 5)

        ops.all(KEY) shouldBe listOf(2, 3, 4, 5)

        ops.rightPush(KEY, 6).awaitSingle()
        ops.all(KEY) shouldBe listOf(2, 3, 4, 5, 6)

        ops.leftPop(KEY).awaitSingle() shouldBe 2
        ops.all(KEY) shouldBe listOf(3, 4, 5, 6)

        ops.leftPush(KEY, 9).awaitSingle()
        ops.all(KEY) shouldBe listOf(9, 3, 4, 5, 6)
        ops.rightPop(KEY).awaitSingle() shouldBe 6
        ops.all(KEY) shouldBe listOf(9, 3, 4, 5)
    }

    "LinkedList LRU Cache" {
        val ops = template.opsForList()
        ops.rightPushAll(KEY,7, 6, 4, 3, 2, 1, 3).awaitSingle()

        ops.remove(KEY, 0, 2).awaitSingle()
        ops.all(KEY) shouldBe listOf(7, 6, 4, 3, 1, 3)

        ops.leftPush(KEY, 2).awaitSingle()
        ops.all(KEY) shouldBe listOf(2, 7, 6, 4, 3, 1, 3)
    }

    "hash" {
        val ops = template.opsForHash<Int, String>()
        val map = (1..10).map { it to "val-$it" }.toMap()
        ops.putAll(KEY, map).awaitSingle()

        ops.size(KEY).awaitSingle() shouldBe 10
        ops.get(KEY, 1).awaitSingle() shouldBe "val-1"
        ops.get(KEY, 8).awaitSingle() shouldBe "val-8"
    }

    "sorted set" {
        // lru 구현
        val ops = template.opsForZSet()
        listOf(8, 7, 1, 4, 13, 22, 9, 7, 8).forEach{
            ops.add(KEY, "$it", -1.0 * Date().time).awaitSingle()
//            ops.all(KEY).let{ logger.debug { it } }
        }

        // 실시간 랭킹 서비스 구현
        template.delete(KEY).awaitSingle()

        listOf(
            "yeonuel"   to      123,
            "hahyo"     to      752,
            "happy"     to      932,
            "john"      to      335,
            "jake"      to      623,
        ).also {
            it.toMap().toList().sortedBy { it.second }.let { logger.debug { "original: $it" } }
        }.forEach {
            ops.add(KEY, it.first, it.second * -1.0).awaitSingle()
            ops.all(KEY).let { logger.debug { it } }
        }
    }

    "geo redis" {// 위경도로 구성된 데이터 간의 거리, 포인트 검색, ... 을 지원
        // 배달 앱 구현, 내 근처에 있는 택시들 구현
        // 위치 기반 서비스 -> Redis, ElasticSearch, ...
        val ops = template.opsForGeo()

        // 이름/경도(longitude)/위도(latitude)
        listOf(
            GeoLocation("seoul",    Point(126.97806, 37.56667)),
            GeoLocation("busan",    Point(129.07556, 35.17944)),
            GeoLocation("incheon",  Point(126.70528, 37.45639)),
            GeoLocation("daegu",    Point(128.60250, 35.87222)),
            GeoLocation("anyang",   Point(126.95556, 37.39444)),
            GeoLocation("daejeon",  Point(127.38500, 36.35111)),
            GeoLocation("gwangju",  Point(126.85306, 35.15972)),
            GeoLocation("suwon",    Point(127.02861, 37.26389))
        ).forEach {
            ops.add(KEY, it as GeoLocation<Any>).awaitSingle()
        }

        ops.distance(KEY, "seoul", "busan").awaitSingle().let { logger.debug { "seoul -> busan: $it" } }

        val p = ops.position(KEY, "daegu").awaitSingle().also { logger.debug { it } }
        val circle = Circle(p, Distance(100.0, Metrics.KILOMETERS))

        ops.radius(KEY, circle).asFlow().map{ it.content.name }.toList().let {
            logger.debug { "cities near daegu: $it" }
        }

    }

    "hyper loglog" {// 정확하진 않지만, 대용량의 빠른 트래픽에 대응해서 카운트할 수 있는 오퍼레이션
        // 페이지 방문 건수 확인하는데 용이함
        val ops = template.opsForHyperLogLog()
        ops.add("page1", "192.179.0.23", "41.61.2.230", "225.105.161.131").awaitSingle()
        ops.add("page2", "1.1.1.1", "2.2.2.2").awaitSingle()
        ops.add("page3", "9.9.9.9").awaitSingle()
        ops.add("page3", "8.8.8.8").awaitSingle()
        ops.add("page3", "7.7.7.7", "2.2.2.2", "1.1.1.1").awaitSingle()

        ops.size("page3").awaitSingle().let { logger.debug { it } }

    }

    "pub / sub" {// pubsub -> Event Queue 시리즈

        // pub / sub을 통해 채팅 구현 가능
        template.listenToChannel("channel-1").doOnNext {
            logger.debug { ">> received 1: $it.message" }
        }.subscribe()

        template.listenToChannel("channel-1").doOnNext {
            logger.debug { ">> received 2: $it.message" }
        }.subscribe()

        template.listenToChannel("channel-1").asFlow().onEach {
            logger.debug { ">> received 3: $it.message" }
        }.launchIn(CoroutineScope(Dispatchers.Default))


        repeat(10) {
            val message = "test message (${it+1})"
            logger.debug { ">> send: $message" }
            template.convertAndSend("channel-1", message).awaitSingle()
            delay(1000)
        }
    }
})

