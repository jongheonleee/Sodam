package sodam.payment_streamer.payment_streamer.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import sodam.payment_streamer.payment_streamer.config.Consumer
import sodam.payment_streamer.payment_streamer.config.TOPIC_PAYMENT

private val logger = KotlinLogging.logger {}

@Configuration
class OrderConsumer(
    private val consumer: Consumer,
    private val historyApi: HistoryApi,
    private val objectMapper: ObjectMapper,
): InitializingBean {

    override fun afterPropertiesSet() {
        consumer.consume(TOPIC_PAYMENT, "es") { record ->
            toOrder(record).let { order ->
                logger.debug { ">> es: $order" }
                historyApi.save(order)
            }
        }

        var total = 0L
        consumer.consume(TOPIC_PAYMENT, "sum") { record ->
            toOrder(record).let { order ->
                logger.debug { ">> sum: $order" }
                if (order.pgStatus == PgStatus.CAPTURE_SUCCESS) {
                    total += order.paidTotAmount
                    logger.debug { "    total: $total" }
                } else {
                    logger.debug { "capture not succeed request!!" }
                }
            }
        }
    }

    private fun toOrder(record: ConsumerRecord<String, String>): OrdersEntity {
        return objectMapper.readValue(record.value(), OrdersEntity::class.java)
    }


}