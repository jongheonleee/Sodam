package sodam.payment_streamer.payment_streamer.config

import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin

const val TOPIC_PAYMENT = "payment"

@Configuration
class TopicConfig(
    private val admin: KafkaAdmin,
): InitializingBean {

    override fun afterPropertiesSet() {
        admin.createOrModifyTopics(
            TopicBuilder.name(TOPIC_PAYMENT)
                        .partitions(1)
                        .replicas(1)
                        .build(),
        )
    }
}