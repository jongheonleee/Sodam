package sodam.backend.payment

import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import sodam.backend.payment.domain.payments.service.PaymentService

@EnableR2dbcAuditing
@SpringBootApplication
class PaymentApplication(
	private val paymentService: PaymentService,
): ApplicationRunner {

	override fun run(args: ApplicationArguments?) {
		runBlocking {
			paymentService.recaptureOnBoot()
		}
	}
}

fun main(args: Array<String>) {
	runApplication<PaymentApplication>(*args)
}
