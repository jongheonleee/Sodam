package sodam.backend.payment.domain.payments.service.api

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class PaymentApi(
    @Value("\${payment.self.domain}")
    private val domain: String,
) {

    private val client = WebClient.builder()
                                  .baseUrl(domain)
                                  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                  .build()

    suspend fun recapture(orderId: String) {
        client.put()
              .uri("/orders/recapture/$orderId")
              .retrieve()
    }
}