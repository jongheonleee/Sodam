package sodam.backend.payment.test.orders.repository

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import sodam.backend.payment.domain.orders.entity.OrdersEntity
import sodam.backend.payment.domain.orders.model.PgStatus.*
import sodam.backend.payment.domain.orders.repository.OrdersRepository
import sodam.backend.payment.domain.orders.service.command.OrderQueryHistory
import sodam.backend.payment.golbal.extension.toLocalDate
import java.time.LocalDateTime
import java.util.*

private val logger = KotlinLogging.logger {}

@SpringBootTest
//@ActiveProfiles("test")
class OrderHistoryCustomRepositoryTest(
    @Autowired private val sut: OrdersRepository,
): StringSpec({

    val testUserId = "2314343@1-e89b-12d3-a456-426614174000"
    val subscruptionId = "3c90e7a4-33f4-11f0-afa4-daa4a654b17b"


    beforeEach {
        sut.deleteAll()
        sut.count() shouldBe 0
    }

    afterEach {
        sut.deleteAll()
        sut.count() shouldBe 0
    }

    "허가된 상태 필터링 테스트 -> CAPTURE_REQUEST, CAPTURE_RETRY, CAPTURE_SUCCESS" {
        // 더미 데이터 추가
        listOf(
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 30_000, discTotAmount = 3_000, paidTotAmount = 27_000, description = "테스트용입니다.", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CREATE, pgRetryCount = 0, orderedAt = LocalDateTime.now()),
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 30_000, discTotAmount = 3_000, paidTotAmount = 27_000, description = "테스트용입니다.", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = PAID, pgRetryCount = 0, orderedAt = LocalDateTime.now()),
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 30_000, discTotAmount = 3_000, paidTotAmount = 27_000, description = "테스트용입니다.", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = AUTH_SUCCESS, pgRetryCount = 0, orderedAt = LocalDateTime.now()),
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 30_000, discTotAmount = 3_000, paidTotAmount = 27_000, description = "테스트용입니다.", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = AUTH_FAILED, pgRetryCount = 0, orderedAt = LocalDateTime.now()),
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 30_000, discTotAmount = 3_000, paidTotAmount = 27_000, description = "테스트용입니다.", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = AUTH_INVALID, pgRetryCount = 0, orderedAt = LocalDateTime.now()),
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 30_000, discTotAmount = 3_000, paidTotAmount = 27_000, description = "테스트용입니다.", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_REQUEST, pgRetryCount = 0, orderedAt = LocalDateTime.now()), // 조회 대상
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 30_000, discTotAmount = 3_000, paidTotAmount = 27_000, description = "테스트용입니다.", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_REQUIRED, pgRetryCount = 0, orderedAt = LocalDateTime.now()),
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 30_000, discTotAmount = 3_000, paidTotAmount = 27_000, description = "테스트용입니다.", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_RETRY, pgRetryCount = 0, orderedAt = LocalDateTime.now()), // 조회 대상
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 30_000, discTotAmount = 3_000, paidTotAmount = 27_000, description = "테스트용입니다.", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_SUCCESS, pgRetryCount = 0, orderedAt = LocalDateTime.now()), // 조회 대상
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 30_000, discTotAmount = 3_000, paidTotAmount = 27_000, description = "테스트용입니다.", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_FAILED, pgRetryCount = 0, orderedAt = LocalDateTime.now())
        ).forEach { sut.insertOnly(it) }

        // 위에 더미 데이터 중에 3개만 조회되야함
        sut.getHistories(OrderQueryHistory(userId = testUserId)).size shouldBe 3
    }

    "사용자 총 주문 이력 조회" {
        var createdAt = "2025-05-20".toLocalDate().atStartOfDay()

        listOf(
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 2_000, discTotAmount = 1_000, paidTotAmount = 1_000, description = "A,B", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_SUCCESS, pgRetryCount = 0, orderedAt = LocalDateTime.now()), // 20
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 2_000, discTotAmount = 900, paidTotAmount = 1_100, description = "C", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_SUCCESS, pgRetryCount = 0, orderedAt = LocalDateTime.now()), // 21
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 2_000, discTotAmount = 800, paidTotAmount = 1_200, description = "D,E,F", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_SUCCESS, pgRetryCount = 0, orderedAt = LocalDateTime.now()), // 22
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 2_000, discTotAmount = 700, paidTotAmount = 1_300, description = "D,G,H", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_SUCCESS, pgRetryCount = 0, orderedAt = LocalDateTime.now()), // 23
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 2_000, discTotAmount = 600, paidTotAmount = 1_400, description = "I,J", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_SUCCESS, pgRetryCount = 0, orderedAt = LocalDateTime.now()), // 24
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 2_000, discTotAmount = 500, paidTotAmount = 1_500, description = "I,L,M", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_SUCCESS, pgRetryCount = 0, orderedAt = LocalDateTime.now()), // 25
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 2_000, discTotAmount = 400, paidTotAmount = 1_600, description = "I,L,M,N", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_SUCCESS, pgRetryCount = 0, orderedAt = LocalDateTime.now()), // 26
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 2_000, discTotAmount = 300, paidTotAmount = 1_700, description = "O", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_SUCCESS, pgRetryCount = 0, orderedAt = LocalDateTime.now()), // 27
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 2_000, discTotAmount = 300, paidTotAmount = 1_800, description = "P,Q", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_SUCCESS, pgRetryCount = 0, orderedAt = LocalDateTime.now()), // 28
            OrdersEntity(orderId = UUID.randomUUID().toString(), userId = testUserId, subscriptionId = subscruptionId, orderTotAmount = 2_000, discTotAmount = 100, paidTotAmount = 1_900, description = "P,R", amount = 10, pgOrderId = UUID.randomUUID().toString(), pgKey = UUID.randomUUID().toString(), pgStatus = CAPTURE_SUCCESS, pgRetryCount = 0, orderedAt = LocalDateTime.now()) // 29
        ).forEach {
            it.pgStatus = CAPTURE_SUCCESS
            sut.insertOnly(it)
            it.createdAt = createdAt
            createdAt = createdAt.plusDays(1)
            sut.save(it)
        }

        sut.count().let { logger.debug { ">> count: $it" } }
        sut.findAll().toList().let { logger.debug { it.joinToString("\n") } }

        sut.getHistories(OrderQueryHistory(userId = testUserId, limit = 20)).size shouldBe 10
        sut.getHistories(OrderQueryHistory(userId = testUserId, keyword = "A")).size shouldBe 1
        sut.getHistories(OrderQueryHistory(userId = testUserId, keyword = "B")).size shouldBe 1
        sut.getHistories(OrderQueryHistory(userId = testUserId, keyword = "C")).size shouldBe 1

        sut.getHistories(OrderQueryHistory(userId = testUserId, keyword = "A")).first().orderId shouldBe
            sut.getHistories(OrderQueryHistory(userId = testUserId, keyword = "B")).first().orderId

        sut.getHistories(OrderQueryHistory(userId = testUserId, keyword = "D")).size shouldBe 2
        sut.getHistories(OrderQueryHistory(userId = testUserId, keyword = "D, H")).size shouldBe 1

        sut.getHistories(OrderQueryHistory(userId = testUserId, keyword = "I")).size shouldBe 3
        sut.getHistories(OrderQueryHistory(userId = testUserId, keyword = "I J")).size shouldBe 1
        sut.getHistories(OrderQueryHistory(userId = testUserId, keyword = "I L")).size shouldBe 2
        sut.getHistories(OrderQueryHistory(userId = testUserId, keyword = "I L N")).size shouldBe 1


        // 페이지 사이즈 2, 3으로 했을 때,
        // 사이즈가 2일 때의 첫번째 데이터와 사이즈가 3일 때의 마지막 데이터가 동일함
        sut.getHistories(OrderQueryHistory(userId = testUserId, limit=2, page=2)).first().orderId shouldBe
            sut.getHistories(OrderQueryHistory(userId = testUserId, limit=3, page=1)).last().orderId

        sut.getHistories(OrderQueryHistory(userId = testUserId, limit=5, page=2)).size shouldBe 5

        sut.getHistories(OrderQueryHistory(userId = testUserId, keyword="I", fromAmount = 1450)).size shouldBe 2
        sut.getHistories(OrderQueryHistory(userId = testUserId, keyword="I", fromAmount = 1450, toAmount = 1600)).size shouldBe 2

        sut.getHistories(OrderQueryHistory(userId = testUserId, fromDate = "2025-05-22")).size shouldBe 8
    }
})