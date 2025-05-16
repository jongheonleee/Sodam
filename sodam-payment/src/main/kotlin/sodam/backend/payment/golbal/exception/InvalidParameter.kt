package sodam.backend.payment.golbal.exception

import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.ResponseStatus
import kotlin.reflect.KProperty

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidParameter(request: Any, field: KProperty<*>, code: String = "", message: String = "") : BindException(
    WebDataBinder(request, request::class.simpleName!!).bindingResult.apply {
        rejectValue(field.name, code, message)
    }
)