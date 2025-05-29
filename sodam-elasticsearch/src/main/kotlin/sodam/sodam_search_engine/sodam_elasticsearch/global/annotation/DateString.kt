package sodam.sodam_search_engine.sodam_elasticsearch.global.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import sodam.sodam_search_engine.sodam_elasticsearch.global.validator.DateValidator
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DateValidator::class])
annotation class DateString(
    val message: String = "not a valid date",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
