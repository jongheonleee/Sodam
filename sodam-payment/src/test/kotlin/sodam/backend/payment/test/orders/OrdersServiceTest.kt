package sodam.backend.payment.test.orders

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import sodam.backend.payment.domain.subscriptions.exception.SubscriptionNotFoundException
import sodam.backend.payment.domain.orders.repository.SubscriptionInOrderRepository
import sodam.backend.payment.domain.orders.service.OrderRequest
import sodam.backend.payment.domain.orders.service.OrderService
import sodam.backend.payment.domain.orders.service.SubscriptionsQuantityRequest
import sodam.backend.payment.domain.subscriptions.repository.SubscriptionRepository

private val logger = KotlinLogging.logger {}

/**
 * 구독권 정보는 무거움, 고정적임
 * 매번 쿼리를 날려서 조회하게 만들어야할까? No
 */

@SpringBootTest
//@ActiveProfiles("test")
class OrdersServiceTest(
    @Autowired private val sut: OrderService,
    @Autowired private val subscriptionsRepository: SubscriptionRepository,
    @Autowired private val subscriptionInOrderRepository: SubscriptionInOrderRepository,
): StringSpec ({

    // 테스트 데이터 아이디
    val testNormalUserId = "123e4567-e89b-12d3-a456-426614174000"
    val subscriptionId1 = "21094738-94af-4739-abe1-6cf9f9773e38" // 해당 아이디에 1년치 가격 데이터도 담겨 있음


    beforeTest {
//        subscriptionsRepository.insertOnly(SubscriptionsEntity(subscriptionId = subscriptionId1, subscriptionName = "BRONZE", subscriptionContent = "브론즈 구독권 혜택 내용", viewCnt = 5, downCnt = 5, price = 10_000))
//        subscriptionsRepository.insertOnly(SubscriptionsEntity(subscriptionId = subscriptionId2, subscriptionName = "SILVER", subscriptionContent = "실버 구독권 혜택 내용", viewCnt = 10, downCnt = 10, price = 15_000))
//        subscriptionsRepository.insertOnly(SubscriptionsEntity(subscriptionId = subscriptionId3, subscriptionName = "GOLD", subscriptionContent = "골드 구독권 혜택 내용", viewCnt = 15, downCnt = 15, price = 20_000))
    }

    afterTest {
//        subscriptionsRepository.deleteById(subscriptionId1)
//        subscriptionsRepository.deleteById(subscriptionId2)
//        subscriptionsRepository.deleteById(subscriptionId3)
    }

    "주문 생성 성공" {
        val request = OrderRequest(
            userId = testNormalUserId,
            subscriptions = listOf(
                SubscriptionsQuantityRequest(subscriptionsId = subscriptionId1, quantity = 1), // 10_000
//                SubscriptionsQuantityRequest(subscriptionsId = subscriptionId2, quantity = 2), // 30_000
//                SubscriptionsQuantityRequest(subscriptionsId = subscriptionId3, quantity = 3), // 60_000
            )
        )

        val order = sut.create(request)
                                    .also { logger.debug { it } }

//        order.orderTotAmount shouldBe 100_000 - 이 부분 저녁에 수정하기(가격 선분이력으로 조회하게 만드는거 처리 해놔야함)
        order.description shouldNotBe null
        order.pgOrderId shouldNotBe null

        subscriptionInOrderRepository.countByOrderId(order.orderId!!) shouldBe 1
    }

    "주문 생성 실패 - 존재하지 않는 subscriptionId 전달" {
        val request = OrderRequest(
            userId = testNormalUserId,
            subscriptions = listOf(
                SubscriptionsQuantityRequest(subscriptionsId = subscriptionId1, quantity = 1),
                SubscriptionsQuantityRequest(subscriptionsId = "not exists subscription id!!", quantity = 3),
            )
        )

        shouldThrow<SubscriptionNotFoundException> {
            sut.create(request)
        }
    }
})