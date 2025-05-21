package sodam.backend.payment.domain.common.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import sodam.backend.payment.domain.orders.controller.toResponse
import sodam.backend.payment.domain.orders.service.OrderService
import sodam.backend.payment.domain.payments.service.PaymentService

@Controller
class ViewController(
    private val orderService: OrderService,
    private val paymentService: PaymentService,
) {

    @GetMapping("/hello/{name}")
    suspend fun hello(@PathVariable name: String, model: Model): String {
        model.addAttribute("name", name)
        model.addAttribute("order", orderService.get("5a56980a-7b9c-442d-ad21-681da4a5308d").toResponse())
        return "hello-world.html"
    }

    @GetMapping("/pay/{orderId}")
    suspend fun pay(@PathVariable("orderId") orderId: String, model: Model): String {
        val order = orderService.get(orderId).toResponse()
        model.addAttribute("order", order)
        return "pay.html"
    }

    @GetMapping("/pay/success")
    suspend fun paySucceed(request: PaySucceedRequest): String {
        if ( ! paymentService.authSucceed(request) )
            return "pay-fail.html"

        paymentService.capture(request)
        return "pay-success.html"
    }

    @GetMapping("/pay/fail")
    suspend fun payFailed(request: PayFailedRequest): String {
        paymentService.authFailed(request)
        return "pay-fail.html"
    }
}

data class PayFailedRequest(
    val code: String,
    val message: String,
    val orderId: String,
)

// {paymentType=[NORMAL], orderId=[c6730559bbde41d2960b0de741325005], paymentKey=[tgen_20250519181255VOgz7], amount=[50000]}
data class PaySucceedRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: Long,
    val paymentType: TossPaymentType,
)

enum class TossPaymentType {
    NORMAL, BRANDPAY, KEYIN
}