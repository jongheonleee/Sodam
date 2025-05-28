package sodam.payment_streamer.payment_streamer

import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka
import sodam.payment_streamer.payment_streamer.produce.TestProducer


@SpringBootApplication
@EnableKafka
class PaymentStreamerApplication(
	private val producer: TestProducer,
): ApplicationRunner {

	override fun run(args: ApplicationArguments?) {
		runBlocking {
			repeat(10) {
				producer.send("test", "test message $it")
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<PaymentStreamerApplication>(*args)
}
