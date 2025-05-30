package sodam.backend.payment.domain.payments.service

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClientRequestException
import org.springframework.web.reactive.function.client.WebClientResponseException
import sodam.backend.payment.domain.common.Beans.Companion.beanOrderService
import sodam.backend.payment.domain.common.KafkaProducer
import sodam.backend.payment.domain.common.controller.PayFailedRequest
import sodam.backend.payment.domain.common.controller.PaySucceedRequest
import sodam.backend.payment.domain.common.controller.TossPaymentType
import sodam.backend.payment.domain.orders.entity.OrdersEntity
import sodam.backend.payment.domain.orders.model.PgStatus.*
import sodam.backend.payment.domain.orders.service.OrderService
import sodam.backend.payment.domain.payments.exception.InvalidOrderStatus
import sodam.backend.payment.domain.payments.service.api.PaymentApi
import sodam.backend.payment.domain.payments.service.api.TossPayApi
import java.time.LocalDateTime
import java.time.Duration

private val logger = KotlinLogging.logger {}

@Service
class PaymentService(
    private val orderService: OrderService, // 이 부분 추후에 제거하기 서비스가 서비스 의존하는 것은 좋지 않음
    private val tossPayApi: TossPayApi,
    private val objectMapper: ObjectMapper,
    private val paymentApi: PaymentApi,
    private val captureMarker: CaptureMarker,
    private val kafkaProducer: KafkaProducer,
) {

    @Transactional
    suspend fun authSucceed(request: PaySucceedRequest): Boolean {
        val order = orderService.getOrderByPgOrderId(request.orderId).apply {
            pgKey = request.paymentKey
            pgStatus = AUTH_SUCCESS
        }

        try {
            // 사용자가 악의적으로 수량을 변경하는 경우
            return if (order.paidTotAmount != request.amount) {
                order.pgStatus = AUTH_INVALID
                logger.error { "Invalid auth because of amount (order: ${order.amount}, pay: ${request.amount}))" }
                false
            } else {
                true
            }
        } finally {
            orderService.save(order) // update 처리
        }

    }

    @Transactional
    suspend fun authFailed(request: PayFailedRequest) {
        val order = orderService.getOrderByPgOrderId(request.orderId)

        if (order.pgStatus == CREATE) {
            order.pgStatus = AUTH_FAILED
            orderService.save(order)
        }

        logger.error { """
            >> Fail on error
                - request: $request, 
                - order: $order
        """.trimMargin() }
    }

    @Transactional
    suspend fun capture(order: OrdersEntity) {
        logger.debug { ">> order: $order" }
        if (order.pgStatus !in setOf(CAPTURE_REQUEST, CAPTURE_RETRY)) {
            throw InvalidOrderStatus("invalid order status (orderId: ${order.orderId}, status: ${order.pgStatus}")
        }
        order.increaseRetryCount()
        captureMarker.put(order.orderId!!)
        try {
            tossPayApi.confirm(order.toPaySucceedRequest())
                .also { logger.debug { ">> response: $it" } }
            order.pgStatus = CAPTURE_SUCCESS
        } catch (e: Exception) {
            // 여기서 부터 재처리
            order.pgStatus = when (e) {
                is WebClientRequestException -> CAPTURE_RETRY

                is WebClientResponseException -> { // 핵심 부분
                    // { code, message } -> UTF8로 인코딩 되어 있음, 따라서 String으로 바로 읽으면 문자 깨짐
                    // ByteArray로 가져온 것을 UTF8 형식으로 전환해야함 (기본값 UTF8로 구성되어 있음)
                    val errorResponse = e.toTossPayApiError()
                    logger.debug { ">> error response: $errorResponse" }

                    when (errorResponse.code) { // 토스 페이 결제 응답 코드 참고
                        "ALREADY_PROCESSED_PAYMENT" -> CAPTURE_SUCCESS
                        "PROVIDER_ERROR", "FAILED_INTERNAL_SYSTEM_PROCESSING" -> CAPTURE_RETRY
                        else -> CAPTURE_FAILED
                    }
                }

                else -> CAPTURE_FAILED
            }
            if (order.pgStatus == CAPTURE_RETRY && order.pgRetryCount >= 3) {
                order.pgStatus = CAPTURE_FAILED
            }
            if (order.pgStatus != CAPTURE_SUCCESS) {
                throw e
            }
        } finally {
            orderService.save(order)
            captureMarker.remove(order.orderId!!)
            if (order.pgStatus == CAPTURE_RETRY) {
                paymentApi.recapture(order.orderId!!)
            }
            logger.debug { ">> call kafka" }
            kafkaProducer.sendPayment(order)
        }
    }

    suspend fun recaptureOnBoot() {
        val now = LocalDateTime.now()
        captureMarker.getAll()
                    .filter { Duration.between(it.modifiedAt!!, now).seconds >= 60 }
                    .forEach {
                        captureMarker.remove(it.orderId!!)
                        paymentApi.recapture(it.orderId!!)
                    }
    }

    @Transactional
    suspend fun capture(request: PaySucceedRequest) {
        val order = orderService.getOrderByPgOrderId(request.orderId).apply {
            pgStatus = CAPTURE_REQUEST
            beanOrderService.save(this) // Propagation.REQUIRES_NEW 적용하게 만든 트릭 - 결국에는 스프링 컨텍스트를 한번 조회하게 만듦
        }

        capture(order)
    }

    private fun OrdersEntity.toPaySucceedRequest(): PaySucceedRequest {
        return this.let {
            PaySucceedRequest(
                paymentKey = it.pgKey!!,
                orderId = it.pgOrderId!!,
                amount = it.paidTotAmount,
                paymentType = TossPaymentType.NORMAL,
            )
        }
    }

    private fun WebClientResponseException.toTossPayApiError(): TossPayApiError {
        val json = String(this.responseBodyAsByteArray)
        return objectMapper.readValue(json, TossPayApiError::class.java)
    }
}

data class TossPayApiError(
    val code: String,
    val message: String,
)