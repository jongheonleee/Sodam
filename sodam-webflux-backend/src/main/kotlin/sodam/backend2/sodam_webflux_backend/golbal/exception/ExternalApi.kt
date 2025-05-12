package sodam.backend2.sodam_webflux_backend.golbal.exception

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.kotlin.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.kotlin.circuitbreaker.executeFunction
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction
import io.github.resilience4j.kotlin.ratelimiter.RateLimiterConfig
import io.github.resilience4j.kotlin.ratelimiter.executeSuspendFunction
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RequestNotPermitted
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders.*
import org.springframework.http.MediaType.*
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private val logger = KotlinLogging.logger {}

@Service
class ExternalApi(
    @Value("\${api.externalUrl}")
    private val externalUrl: String,
) {

    private val client = WebClient.builder()
                                  .baseUrl(externalUrl)
                                  .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                                  .build()

    suspend fun delay() {
        return client.get().uri("/delay").retrieve().awaitBody()
    }

    suspend fun testCircuitBreaker(flag: String): String {
        logger.debug { "1. request call" }
        return try {
            rateLimiter.executeSuspendFunction {
                circuitBreaker.executeSuspendFunction{
                    logger.debug { "2. call external" }
                    client.get().uri("/test/circuit/child/$flag").retrieve().awaitBody()
                }
            }
        } catch (e: CallNotPermittedException) { // CircuitBreaker 차단 오류가 발생할 때, 값 전달하기
            "call later (blocked by circuit breaker)"
        } catch (e: RequestNotPermitted) {
            "call later (blocked by rate limiter)"
        }
    }

    /**
     * - close: 회로가 닫힘 -> 정상
     * - open: 회로가 열림 -> 차단
     * - half-open: 반열림 -> 중간중간에 찔러보기
     */
    val circuitBreaker = CircuitBreaker.of("test", CircuitBreakerConfig {
        // 10번의 호출동안 20%로, 즉, 2번의 호출 실패가 일어나면
        // 회로가 open 되면서 외부 호출 차단
        slidingWindowSize(10)
        failureRateThreshold(20.0F)
        // open (차단 상태) -> close (열림 상태) 변경 : half-open 상태
        waitDurationInOpenState(10.seconds.toJavaDuration())
        // half-open 상태에서 허용할 요청 수
        permittedNumberOfCallsInHalfOpenState(3)
    })

    // 너무 과한 호출은 RateLimiter 에 의해 차단됨
    val rateLimiter = RateLimiter.of("rps-limiter", RateLimiterConfig {
        limitForPeriod(2)
        timeoutDuration(5.seconds.toJavaDuration())
        limitRefreshPeriod(10.seconds.toJavaDuration())
    })
}