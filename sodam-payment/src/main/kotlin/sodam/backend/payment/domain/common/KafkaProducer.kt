package sodam.backend.payment.domain.common

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.reactor.awaitSingle
import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Component
import reactor.kafka.sender.SenderOptions
import sodam.backend.payment.domain.orders.entity.OrdersEntity

private val logger = KotlinLogging.logger {}

@Component
class KafkaProducer(
    private val template: ReactiveKafkaProducerTemplate<String, String>,
    private val mapper: ObjectMapper,
) {

    suspend fun send(topic: String, message: String) {
        logger.debug { "topic: $topic, message: $message" }
        val result = template.send(topic, message)
                                                  .awaitSingle()
        logger.debug { "send result: $result" }
    }

    suspend fun sendPayment(order: OrdersEntity) {
        mapper.writeValueAsString(order).let { json ->
            logger.debug { "json: $json" }
            send("payment", json)
        }
    }
}

@Configuration
class ReactiveKafkaInitializer {

    @Bean
    fun reactiveProducer(properties: KafkaProperties): ReactiveKafkaProducerTemplate<String, String> {
        return properties.buildProducerProperties()
            .let { prop ->
                prop[ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG] = true
                SenderOptions.create<String, String>(prop)
            }
            .let { option -> ReactiveKafkaProducerTemplate(option) }
    }
}