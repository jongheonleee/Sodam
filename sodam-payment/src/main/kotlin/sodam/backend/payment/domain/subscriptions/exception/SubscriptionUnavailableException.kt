package sodam.backend.payment.domain.subscriptions.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.FORBIDDEN)
class SubscriptionUnavailableException(message: String) : Throwable(message)
