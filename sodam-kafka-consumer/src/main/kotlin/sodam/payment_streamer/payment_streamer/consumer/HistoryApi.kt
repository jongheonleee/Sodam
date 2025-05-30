package sodam.payment_streamer.payment_streamer.consumer

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitExchange
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

@Service
class HistoryApi(
    @Value("\${api.history.domain}")
    private val domain: String,
) {

    private val client = WebClient.builder()
                                  .baseUrl(domain)
                                  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                  .build()

    suspend fun save(order: OrdersEntity) {
        logger.debug { ">> call es server" }
        client.post()
              .uri("/order-history")
              .bodyValue(order.toOrderHistoryRequest())
              .awaitExchange {  }
    }
}

data class OrdersEntity(
    var orderId: String? = null,
    var userId: String? = null,
    var socialUserId: String? = null,
    var subscriptionId: String? = null, // 이 부분 제거해야함
    var orderTotAmount: Long = 0,
    var discTotAmount: Long = 0,
    var paidTotAmount: Long = 0,
    var description: String? = null, // 이거 필요함
    var amount: Long = 0,
    var pgOrderId: String? = null,
    var pgKey: String? = null, // 이거 필요함
    var pgStatus: PgStatus = PgStatus.CREATE, // 이거 필요함
    var pgRetryCount: Int = 0,
    var orderedAt: LocalDateTime? = LocalDateTime.now(),
    var createdAt: LocalDateTime? = null,
    var modifiedAt: LocalDateTime? = null,
) {

    fun toOrderHistoryRequest(): OrdersHistoryRequest {
        return this.let { OrdersHistoryRequest(
            orderId = orderId!!,
            userId = userId,
            socialUserId = socialUserId,
            orderTotAmount = orderTotAmount,
            discTotAmount = discTotAmount,
            paidTotAmount = paidTotAmount,
            description = description,
            pgStatus = pgStatus,
            orderedAt = orderedAt,
            createdAt = createdAt!!,
            modifiedAt = modifiedAt!!,
        ) }
    }
}

enum class PgStatus {
    CREATE,
    PAID,
    AUTH_SUCCESS,
    AUTH_FAILED,
    AUTH_INVALID,
    CAPTURE_REQUEST,
    CAPTURE_REQUIRED,
    CAPTURE_RETRY,
    CAPTURE_SUCCESS,
    CAPTURE_FAILED,
}


data class OrdersHistoryRequest(
    var orderId: String,
    var userId: String?,
    var socialUserId: String?,
    var orderTotAmount: Long?,
    var discTotAmount: Long?,
    var paidTotAmount: Long?,
    var description: String?,
    var pgStatus: PgStatus?,
    var orderedAt: LocalDateTime?,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var modifiedAt: LocalDateTime = LocalDateTime.now(),
)