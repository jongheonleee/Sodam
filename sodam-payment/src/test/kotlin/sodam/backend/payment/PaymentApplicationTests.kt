package sodam.backend.payment

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionHistoryEntity
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionPriceEntity
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionStatusEntity
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionsEntity
import sodam.backend.payment.domain.subscriptions.repository.SubscriptionHistoryRepository
import sodam.backend.payment.domain.subscriptions.repository.SubscriptionPriceRepository
import sodam.backend.payment.domain.subscriptions.repository.SubscriptionRepository
import sodam.backend.payment.domain.subscriptions.repository.SubscriptionStatusRepository
import java.time.LocalDateTime
import java.util.*

private val logger = KotlinLogging.logger {}

@SpringBootTest
//@ActiveProfiles("test")
class PaymentApplicationTests(
	// 테스트할 대상 주입 - 각 도메인 별 repository
	@Autowired private val subscriptionRepository: SubscriptionRepository,
	@Autowired private val subscriptionPriceRepository: SubscriptionPriceRepository,
	@Autowired private val subscriptionHistoryRepository: SubscriptionHistoryRepository,
	@Autowired private val subscriptionStatusRepository: SubscriptionStatusRepository,
): StringSpec ({

	val subscriptionId = "eedc7403-8ea3-4f59-a50e-40134670a5a7"

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

	"subscriptions price" {
		val subscription = subscriptionRepository.findById(subscriptionId) // 현재 등록되어 있는 실버 구독권 id
		var prevCnt = subscriptionPriceRepository.count()
		val subscriptionPrice = SubscriptionPriceEntity(
			subscriptionId = subscription!!.subscriptionId,
			price = 15_000,
			discRate = 0.3F,
			discPrice = (15_000 - (15_000 * 0.3f)).toLong(),
			salePrice = (15_000 * 0.3f).toLong(),
			validYN = true,
			startAt = LocalDateTime.now(),
			endAt = LocalDateTime.now().plusMonths(3),
		)

		subscriptionPriceRepository.save(subscriptionPrice)
								   .also { logger.debug { it } }

		var currCnt = subscriptionPriceRepository.count()
		currCnt shouldBe prevCnt + 1
	}

	"subscriptions history" {
		val subscription = subscriptionRepository.findById(subscriptionId) // 현재 등록되어 있는 실버 구독권 id
		var prevCnt = subscriptionHistoryRepository.count()
		val subscriptionsHistory = SubscriptionHistoryEntity(
			subscriptionId = subscription!!.subscriptionId,
			startAt = LocalDateTime.now(),
			endAt = LocalDateTime.now().plusMonths(3),
			subscriptionName = "테스트용입니다.",
			downCnt = 10,
			viewCnt = 10,
			subscriptionDesc = "테스트용입니다."
		)

		subscriptionHistoryRepository.save(subscriptionsHistory)
									 .also { logger.debug { it } }

		var currCnt = subscriptionHistoryRepository.count()
		currCnt shouldBe prevCnt + 1
	}

	"subscriptions status" {
		val subscription = subscriptionRepository.findById(subscriptionId) // 현재 등록되어 있는 실버 구독권 id
		var prevCnt = subscriptionStatusRepository.count()
		val subscriptionStatus = SubscriptionStatusEntity(
			subscriptionId = subscription!!.subscriptionId,
			startAt = LocalDateTime.now(),
			endAt = LocalDateTime.now().plusMonths(3),
			validYN = true,
		)

		subscriptionStatusRepository.save(subscriptionStatus)
									.also { logger.debug { it } }

		var currCnt = subscriptionStatusRepository.count()
		currCnt shouldBe prevCnt + 1

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
