package sodam.backend.payment.domain.payments.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
class InvalidOrderStatus(message: String) : Throwable(message) {

}
