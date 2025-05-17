package sodam.backend.payment

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionPriceEntity
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionsEntity
import sodam.backend.payment.domain.subscriptions.repository.SubscriptionPriceRepository
import sodam.backend.payment.domain.subscriptions.repository.SubscriptionRepository
import java.util.*

private val logger = KotlinLogging.logger {}

@SpringBootTest
//@ActiveProfiles("test")
class PaymentApplicationTests(
	// 테스트할 대상 주입 - 각 도메인 별 repository
	@Autowired private val subscriptionRepository: SubscriptionRepository,
): StringSpec ({


	// 간단하게 CRUD 기능 테스트
	// - 하나 생성, 카운팅 비교, 삭제 처리
	"subscriptions" {
		var prevCnt = subscriptionRepository.count()
		val subscription = SubscriptionsEntity(
			subscriptionId = UUID.randomUUID().toString(),
			subscriptionName = "테스트용",
			subscriptionContent = "테스트용",
			viewCnt = 0,
			downCnt = 0,
		)
		subscriptionRepository.insertOnly(subscription).also { logger.debug { it } }
		var currCnt = subscriptionRepository.count()
		currCnt shouldBe prevCnt + 1


		subscriptionRepository.delete(subscription)
		currCnt = subscriptionRepository.count()
		currCnt shouldBe prevCnt
	}

	"order" {

	}

	"payments" {

	}


}) {


	@Test
	fun contextLoads() {

	}
}
