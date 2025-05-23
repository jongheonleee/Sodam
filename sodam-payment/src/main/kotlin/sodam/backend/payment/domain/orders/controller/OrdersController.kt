package sodam.backend.payment.domain.orders.controller

import kotlinx.coroutines.delay
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import sodam.backend.payment.domain.common.Beans.Companion.beanSubscriptionInOrderRepository
import sodam.backend.payment.domain.common.Beans.Companion.beanSubscriptionService
import sodam.backend.payment.domain.orders.controller.request.OrderRequest
import sodam.backend.payment.domain.orders.entity.OrdersEntity
import sodam.backend.payment.domain.orders.service.OrderService
import sodam.backend.payment.domain.orders.service.command.OrderQueryHistory
import sodam.backend.payment.domain.orders.service.response.OrderResponse
import sodam.backend.payment.domain.orders.service.response.SubscriptionQuantityResponse
import sodam.backend.payment.domain.payments.service.CaptureMarker
import sodam.backend.payment.domain.payments.service.PaymentService
import kotlin.time.Duration
import kotlin.math.pow
import kotlin.time.Duration.Companion.milliseconds


private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/orders")
class OrdersController(
    private val orderService: OrderService,
    private val paymentService: PaymentService,
    private val captureMarker: CaptureMarker,
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

    @GetMapping("/history")
    suspend fun getHistories(request: OrderQueryHistory): List<OrdersEntity> {
        return orderService.getHistories(request)
    }

    // 이 부분 추후에 AOP를 사용하든, 데코레이터 패턴을 활용하든 분리하자
    // 컨트롤러나 서비스에서 담기 애매한 코드임
    @PutMapping("/recapture/{orderId}")
    suspend fun recapture(@PathVariable orderId: String) {
        orderService.get(orderId).let {
            logger.debug { ">> recapture : $it" }
            val time = getBackoffDelay(it)
            delay(1_000).also { logger.debug { ">> delay: $it ms" } }
            paymentService.capture(it)
        }
    }

    // backoff 전략
    // - temp = 2 ^ retry count
    // - delay = (temp / 2) + (0 .. (temp/2)).random
    private fun getBackoffDelay(order: OrdersEntity): Duration {
        val temp = (2.0).pow(order.pgRetryCount).toInt() * 1_000
        val delay = temp + (0 .. temp).random()
        return delay.milliseconds
    }

    @GetMapping("/capturing")
    suspend fun getCapturingOrder(): List<OrdersEntity> {
        return captureMarker.getAll()
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
        )
    }
}

