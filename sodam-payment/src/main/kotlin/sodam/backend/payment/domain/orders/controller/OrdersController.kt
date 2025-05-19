package sodam.backend.payment.domain.orders.controller

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import sodam.backend.payment.domain.common.Beans.Companion.beanSubscriptionInOrderRepository
import sodam.backend.payment.domain.common.Beans.Companion.beanSubscriptionService
import sodam.backend.payment.domain.orders.entity.OrdersEntity
import sodam.backend.payment.domain.orders.model.PgStatus
import sodam.backend.payment.domain.orders.service.OrderRequest
import sodam.backend.payment.domain.orders.service.OrderService
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/orders")
class OrdersController(
    private val orderService: OrderService,
) {

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun create(@RequestBody request: OrderRequest): OrderResponse {
        return orderService.create(request)
                           .toResponse()
    }

    @GetMapping("/{orderId}")
    suspend fun get(@PathVariable("orderId") orderId: String): OrderResponse {
        return orderService.get(orderId)
                           .toResponse()
    }

    @GetMapping("/all/{userId}")
    suspend fun getAll(@PathVariable("userId") userId: String): List<OrderResponse> {
        return orderService.getAll(userId)
                           .map { it.toResponse() }
    }

    @DeleteMapping("/{orderId}")
    suspend fun delete(@PathVariable("orderId") orderId: String) {
        orderService.delete(orderId)
    }
}

// 해당 부분 문제 해결 대상 - 스프링 컨테이너가 관리하지 않는 오브젝트에서 빈을 어떻게 사용할 수 있을까
// - Beans라는 클래스 정의, Method Area에 올림, 스태틱 필드 활용
// - 빈을 타입으로 탐색해서 사용하고자 하는 빈을 조회한 다음에 기능 사용하게끔 구성
suspend fun OrdersEntity.toResponse(): OrderResponse {
    return this.let {
        OrderResponse(
            orderId = it.orderId!!,
            userId = it.userId,
            socialUserId = it.socialUserId,
            subscriptionId = it.subscriptionId!!,
            orderTotAmount = it.orderTotAmount,
            discTotAmount = it.discTotAmount,
            paidTotAmount = it.paidTotAmount,
            description = it.description!!,
            amount = it.amount,
            pgOrderId = it.pgOrderId!!,
            pgKey = it.pgKey,
            pgStatus = it.pgStatus,
            pgRetryCount = it.pgRetryCount,
            orderedAt =it.orderedAt,
            createdAt = it.createdAt,
            modifiedAt = it.modifiedAt,
            subscriptions = beanSubscriptionInOrderRepository.findAllByOrderId(it.orderId!!).map { subscriptionInOrder ->
                SubscriptionQuantityResponse(
                    subscriptionId = subscriptionInOrder.subscriptionId!!,
                    subscriptionName = beanSubscriptionService.get(subscriptionInOrder.subscriptionId!!)?.subscriptionName ?: "unknown",
                    price = subscriptionInOrder.orderPrice!!,
                    quantity = subscriptionInOrder.orderAmount!!,
                )
            },
        )}
}


data class OrderResponse(
    val orderId: String,
    val userId: String? = null,
    val socialUserId: String? = null,
    val subscriptionId: String,
    val orderTotAmount: Long = 0,
    val discTotAmount: Long = 0,
    val paidTotAmount: Long = 0,
    val description: String, // 이거 필요함
    val amount: Long = 0,
    val pgOrderId: String,
    val pgKey: String? = null, // 이거 필요함
    val pgStatus: PgStatus, // 이거 필요함
    val pgRetryCount: Int = 0,
    val orderedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime? = null,
    val modifiedAt: LocalDateTime? = null,
    val subscriptions: List<SubscriptionQuantityResponse>,
)

data class SubscriptionQuantityResponse(
    val subscriptionId: String,
    val subscriptionName: String,
    val price: Long,
    val quantity: Long,
)