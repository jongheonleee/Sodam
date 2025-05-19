package sodam.backend.payment.domain.orders.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import sodam.backend.payment.domain.common.Beans.Companion.beanOrderService
import sodam.backend.payment.domain.common.controller.PaySucceedRequest
import sodam.backend.payment.domain.orders.entity.OrdersEntity
import sodam.backend.payment.domain.orders.entity.SubscriptionInOrderEntity
import sodam.backend.payment.domain.orders.exception.OrderNotFoundExceptions
import sodam.backend.payment.domain.subscriptions.exception.SubscriptionNotFoundException
import sodam.backend.payment.domain.orders.model.PgStatus
import sodam.backend.payment.domain.orders.repository.OrdersRepository
import sodam.backend.payment.domain.orders.repository.SubscriptionInOrderRepository
import sodam.backend.payment.domain.payments.service.TossPayApi
import sodam.backend.payment.domain.subscriptions.exception.SubscriptionUnavailableException
import sodam.backend.payment.domain.subscriptions.repository.SubscriptionRepository
import sodam.backend.payment.domain.subscriptions.service.SubscriptionService
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class OrderService(
    private val ordersRepository: OrdersRepository,
    private val subscriptionService: SubscriptionService, // 이 부분 나중에 repository 쓰게끔 만들어야함
    private val subscriptionRepository: SubscriptionRepository,
    private val subscriptionInOrderRepository: SubscriptionInOrderRepository,
    private val tossPayApi: TossPayApi,
) {

    @Transactional
    suspend fun create(request: OrderRequest): OrdersEntity {
        // 구독권이 존재하는지 확인
        request.subscriptions.forEach {
            val isExists = subscriptionRepository.existsById(it.subscriptionsId)
            if (! isExists) {
                throw SubscriptionNotFoundException(it.subscriptionsId)
            }
        }

        // 밑에는 DB 계속 조회하는 형태임, 하지만, 구독권 정보는 특성상 무겁고 고정적인데 매번 DB에 쿼리를 날리는 것은 비효율적임
        // 캐시 활용해서 DB 부하 억제하기
//        val subscriptionsById = subscriptionRepository.findAllById(subscriptionIds)
//                                                                                    .toList()
//                                                                                    .associateBy { it.subscriptionId }

        val subscriptionSaleInfoById = request.subscriptions
                                                                      .mapNotNull { subscriptionService.get(it.subscriptionsId) } // 1차로 캐시 히트 노림, 2차로는 DB 쿼리 날림
                                                                      .associateBy { it.subscriptionId } // 마지막에 id를 키값으로 갖는 맵 생성

        // 판매 가능 여부 확인
        subscriptionSaleInfoById.forEach { subscriptionId, subscriptionSaleInfo ->
            if (! subscriptionSaleInfo.isSale) {
                throw SubscriptionUnavailableException("subscriptionId : ${subscriptionId}는 판매가 중지된 구독권입니다.")
            }
        }

        // 주문 총 금액 계산(아직, 선분이력으로 가격 계산안함(임시적으로 구독권에 가격 정보 담고 해당 정보로 계산 - 추후에 판매 가능 여부 확인
        // 총 원가 계산
        val totalOrderPrice = request.subscriptions
                                            .map { subscriptionSaleInfoById[it.subscriptionsId]!!.price * it.quantity } // 가격 * 수량 -> 주문 금액
                                            .sum()

        // 총 할인 금액 계산
        val totalDiscPrice = request.subscriptions
                                           .map{ subscriptionSaleInfoById[it.subscriptionsId]!!.discPrice * it.quantity }
                                           .sum()

        // 최종 결제 금액 계산
        val totalPaidPrice = totalOrderPrice - totalDiscPrice

        // 전체 구독권 개수
        val totalSubscriptionCnt = request.subscriptions
                                                .map { it.quantity }
                                                .sum()

       //  "bronze x 2, silver x 3" 형식으로 설명문 생성
        val orderDescription = request.subscriptions
                                         .map { "${subscriptionSaleInfoById[it.subscriptionsId]!!.subscriptionName} * ${it.quantity}" }
                                         .joinToString(", ")

        val newOrder = ordersRepository.insertOnly(
            OrdersEntity(
                orderId = UUID.randomUUID().toString(),
                userId = request.userId,
                subscriptionId = "이 부분 제거해야함 설계 잘못함",
                orderTotAmount = totalOrderPrice,
                discTotAmount = totalDiscPrice,
                paidTotAmount = totalPaidPrice,
                description = orderDescription,
                amount = totalSubscriptionCnt,
                pgOrderId = "${UUID.randomUUID()}".replace("-", ""),
                pgStatus = PgStatus.CREATE,
            )
        )

        request.subscriptions.forEach {
            subscriptionInOrderRepository.save(
                SubscriptionInOrderEntity(
                    orderId = newOrder.orderId,
                    subscriptionId = it.subscriptionsId,
                    orderPrice = subscriptionSaleInfoById[it.subscriptionsId]!!.salePrice, // 최종 결제(주문) 금액
                    orderAmount = it.quantity,
                )
            )
        }

        return newOrder
    }

    // 해당 부분 문제 해결 대상 - 특정 오브젝트 내에서 메서드 호출 & TX 묶여있을 때 TX 적용되지 않는 문제
    // - @Transactional은 AOP 기술임
    // - 따라서, 두 개의 서로 다른 TX 적용하려면 같은 객체의 메서드를 호출하더라도 스프링 컨테이너 거쳐서 호출해야함
    // - 그래야만, AOP 적용 가능함
    // -> 약간의 트릭을 써야함
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    suspend fun save(order: OrdersEntity) {
        ordersRepository.save(order)
    }

    @Transactional
    suspend fun capture(request: PaySucceedRequest): Boolean {
        val order = getOrderByPgOrderId(request.orderId).apply {
            pgStatus = PgStatus.CAPTURE_REQUEST
            beanOrderService.save(this) // Propagation.REQUIRES_NEW 적용하게 만든 트릭 - 결국에는 스프링 컨텍스트를 한번 조회하게 만듦
        }
        logger.debug { ">> order: $order" }

        return try {
            tossPayApi.confirm(request).also { logger.debug { ">> response: $it" } }
            order.pgStatus = PgStatus.CAPTURE_SUCCESS
            true
        } catch (e: Exception) {
            logger.error(e.message, e)
            order.pgStatus = when {
                e is WebClientRequestException -> PgStatus.CAPTURE_RETRY
                e is WebClientResponseException -> PgStatus.CAPTURE_FAILED
                else -> PgStatus.CAPTURE_FAILED
            }
            false
        } finally {
            ordersRepository.save(order)
        }
//        ordersRepository.save(order)
    }

    @Transactional
    suspend fun authSucceed(request: PaySucceedRequest): Boolean {
        val order = getOrderByPgOrderId(request.orderId).apply {
            pgKey = request.paymentKey
            pgStatus = PgStatus.AUTH_SUCCESS
        }

        try {
            // 사용자가 악의적으로 수량을 변경하는 경우
            return if (order.paidTotAmount != request.amount) {
                order.pgStatus = PgStatus.AUTH_INVALID
                logger.error { "Invalid auth because of amount (order: ${order.amount}, pay: ${request.amount}))" }
                false
            } else {
                true
            }
        } finally {
            ordersRepository.save(order) // update 처리
        }

    }

    suspend fun getOrderByPgOrderId(pgOrderId: String): OrdersEntity {
        return ordersRepository.findByPgOrderId(pgOrderId) ?:
            throw OrderNotFoundExceptions("pgOrderId: $pgOrderId")
    }

    suspend fun get(orderId: String): OrdersEntity {
        logger.debug { "orderId : $orderId" }
        return ordersRepository.findById(orderId) ?: throw OrderNotFoundExceptions("")
    }

    suspend fun getAll(userId: String): List<OrdersEntity> {
        return ordersRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
    }

    suspend fun delete(orderId: String) {
        ordersRepository.deleteById(orderId)
    }
}

data class OrderRequest(
    val userId: String,
    var subscriptions: List<SubscriptionsQuantityRequest>,
)

data class SubscriptionsQuantityRequest(
    val subscriptionsId: String,
    val quantity: Long,
)