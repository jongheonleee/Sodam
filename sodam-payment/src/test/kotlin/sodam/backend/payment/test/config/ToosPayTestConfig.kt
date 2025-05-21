package sodam.backend.payment.test.config

import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import sodam.backend.payment.domain.payments.service.api.TossPayApi


// 테스트 환경에서 목객체 활용하기
// 문제해결 내용 작성
@Configuration
@Profile("toss-pay-test")
class ToosPayTestConfig {
    
    @Bean
    @Primary
    fun testTossPayTestApi(): TossPayApi {
        return Mockito.mock(TossPayApi::class.java)
    }
}