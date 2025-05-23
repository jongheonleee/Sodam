package sodam.backend.payment.domain.payments.service.api

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import sodam.backend.payment.domain.orders.service.OrderService
import sodam.backend.payment.domain.payments.service.CaptureMarker

@Service
class PaymentApi(
    @Value("\${payment.self.domain}")
    private val domain: String,
    private val captureMarker: CaptureMarker,
    private val orderService: OrderService,
) {

    private val client = WebClient.builder()
                                  .baseUrl(domain)
                                  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                  .build()

    suspend fun recapture(orderId: String) {
        captureMarker.put(orderId)
        client.put()
              .uri("/orders/recapture/$orderId")
              .retrieve()
    }
}