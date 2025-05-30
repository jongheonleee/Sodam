package sodam.payment_streamer.payment_streamer

import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka
import sodam.payment_streamer.payment_streamer.config.Consumer

private val logger = KotlinLogging.logger {}


@SpringBootApplication
@EnableKafka
class PaymentStreamerApplication
//
//	(
//	private val consumer: Consumer,
//): ApplicationRunner {
//
//	override fun run(args: ApplicationArguments?) {
//		consumer.consume("test", "A") {
//			logger.debug { ">> [A] got message: $it!!" }
//		}
//
//		consumer.consume("test", "B") {
//			logger.debug { ">> [B] got message: $it!!" }
//		}
//		logger.debug { ">> ready consumer!!" }
//	}
//}

fun main(args: Array<String>) {
	runApplication<PaymentStreamerApplication>(*args)
}
