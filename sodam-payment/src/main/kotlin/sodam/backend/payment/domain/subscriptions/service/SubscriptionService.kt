package sodam.backend.payment.domain.subscriptions.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionsEntity
import sodam.backend.payment.domain.subscriptions.repository.SubscriptionRepository
import sodam.backend.payment.domain.subscriptions.service.response.SubscriptionSaleInfo
import sodam.backend.payment.golbal.config.CacheKey
import sodam.backend.payment.golbal.config.CacheManager
import kotlin.time.Duration.Companion.minutes


@Service
class SubscriptionService(
    private val subscriptionRepository: SubscriptionRepository,
    private val cacheManager: CacheManager,
    @Value("\${spring.active.profile:local}") // 로컬, 테스트 환경 분리해서 사용하기
    private val profile: String,
) {

    val CACHE_KEY = "${profile}/payment/subscriptions".also { cacheManager.ttl[it] = 10.minutes } // 가격 선분 이력으로 기록함. 보통 3개월 단위로 변동되기 때문에 이를 고려해서 넣기

    // 해당 부분 문제 해결 대상 - 구독권 상품 정보 조회 기능
    // - 3개의 조인이 걸려있는 상황
    // - 꽤 무거운 작업. 또한, DB CPU를 쓰고 있기 때문에 많은양의 요청이 몰리는 순간 DB 부하 높아짐
    // - 또한, 데이터 특성상 변경이 자주일어나지 않음. 물론, 가격 측면에서는 1년에 4분기 마다 업데이트함
    // - 그래도 정적인 데이터라 판단함
    // -> 따라서, 해당 데이터들은 레디스에 올려서 캐시히트로 성능 개선하는 것이 주요 관건
    suspend fun get(subscriptionId: String): SubscriptionSaleInfo? {
        val key = CacheKey(CACHE_KEY, subscriptionId)
        return cacheManager.get(key) { // 1차적으로 cache 에서 조회함. 하지만, 없을 경우에는 DB 조회 처리
            subscriptionRepository.selectSubscriptionSaleInfo(subscriptionId)
        }
    }
}