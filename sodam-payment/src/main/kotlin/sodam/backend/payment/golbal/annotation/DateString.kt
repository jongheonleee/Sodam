package sodam.backend.payment.golbal.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import sodam.backend.payment.golbal.validator.DateValidator
import kotlin.reflect.KClass

// 일반적인 필드의 경우엔 이렇게 어노테이션 정의하고 validator 구현해서 처리하는게 용이함
// 하지만, 업무로직을 갖는 필드의 경우는 위와같이 구현하기 어려움
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DateValidator::class])
annotation class DateString(
    val message: String = "not a valid date",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
