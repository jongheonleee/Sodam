package sodam.backend.payment.test.init

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import sodam.backend.payment.domain.orders.entity.OrdersEntity
import sodam.backend.payment.domain.orders.entity.SubscriptionInOrderEntity
import sodam.backend.payment.domain.orders.model.PgStatus
import sodam.backend.payment.domain.orders.repository.OrdersRepository
import sodam.backend.payment.domain.orders.repository.SubscriptionInOrderRepository
import sodam.backend.payment.domain.payments.entity.PaymentsEntity
import sodam.backend.payment.domain.payments.repository.PaymentsHistoryRepository
import sodam.backend.payment.domain.payments.repository.PaymentsRepository
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
class SodamPaymentApplicationInitTests(
	// 테스트할 대상 주입 - 각 도메인 별 repository
	// [1] 구독권
	@Autowired private val subscriptionRepository: SubscriptionRepository,
	@Autowired private val subscriptionPriceRepository: SubscriptionPriceRepository,
	@Autowired private val subscriptionHistoryRepository: SubscriptionHistoryRepository,
	@Autowired private val subscriptionStatusRepository: SubscriptionStatusRepository,
	@Autowired private val subscriptionInOrderRepository: SubscriptionInOrderRepository,

	// [2] 주문
	@Autowired private val ordersRepository: OrdersRepository,

	// [3] 결제
	@Autowired private val paymentsRepository: PaymentsRepository,
): StringSpec({


	val subscriptionId = "3c90e7a4-33f4-11f0-afa4-daa4a654b17b"
	val normalUserId = "123e4567-e89b-12d3-a456-426614174000"
	val orderId = "f1a9c62b-12a3-4a97-b923-b7a1d0a1f111"

	// 간단하게 CRUD 기능 테스트
	// - 하나 생성, 카운팅 비교, 삭제 처리

	// [1] 구독권
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
		var prevCnt = subscriptionStatusRepository.count()
		val subscriptionStatus = SubscriptionStatusEntity(
            subscriptionId = subscriptionId,
            startAt = LocalDateTime.now(),
            endAt = LocalDateTime.now().plusMonths(3),
            validYN = true,
        )

		subscriptionStatusRepository.save(subscriptionStatus)
									.also { logger.debug { it } }

		var currCnt = subscriptionStatusRepository.count()
		currCnt shouldBe prevCnt + 1

	}


	// [2] 주문
	"orders" {
		val prevCnt = ordersRepository.count()
		val orders = OrdersEntity(
			orderId = UUID.randomUUID().toString(),
			userId = normalUserId,
			subscriptionId = subscriptionId,
			orderTotAmount = 10,
			discTotAmount = 10,
			paidTotAmount = 10,
			description = "테스트용입니다.",
			amount = 10,
			pgOrderId = UUID.randomUUID().toString(),
			pgKey = UUID.randomUUID().toString(),
			pgStatus = PgStatus.CREATE,
			pgRetryCount = 0,
			orderedAt = LocalDateTime.now(),
		)
		ordersRepository.insertOnly(orders)
					    .also { logger.debug { it } }

		var currCnt = ordersRepository.count()
		currCnt shouldBe prevCnt + 1
	}

	"orders history" {

	}


	"orders status" {

	}

	"subscription in order" {
		var subscriptionInOrder = SubscriptionInOrderEntity(
			subscriptionId = subscriptionId,
			orderId = orderId,
			orderPrice = 15_000,
			orderAmount = 10,
		)
		var prevCnt = subscriptionInOrderRepository.count()
		subscriptionInOrderRepository.save(subscriptionInOrder)
									 .also { logger.debug { it } }

		var currCnt = subscriptionInOrderRepository.count()
		currCnt shouldBe prevCnt + 1
	}


	// [3] 결제
	"payments" {
		var prevCnt = paymentsRepository.count()
		val payments = PaymentsEntity(
			paymentId = UUID.randomUUID().toString(),
			userId = normalUserId,
			orderId = orderId,
			paymentAmount = 10,
			paymentCode = "PAYMENT_CODE",
			cardApprCode = "CARD_APPR_CODE",
			cardCancCode = "CARD_CANC_CODE",
			subscriptionId = subscriptionId,
			paydStat = "CREATED"
		)
		paymentsRepository.insertOnly(payments)
						  .also { logger.debug { it } }
		var currCnt = paymentsRepository.count()
		currCnt shouldBe prevCnt + 1
	}

	"payments history" {

	}


})