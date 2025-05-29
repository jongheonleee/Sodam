package sodam.sodam_search_engine.sodam_elasticsearch.global.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import sodam.sodam_search_engine.sodam_elasticsearch.global.annotation.DateString
import sodam.sodam_search_engine.sodam_elasticsearch.global.extension.toLocalDate
import sodam.sodam_search_engine.sodam_elasticsearch.global.extension.toString

class DateValidator: ConstraintValidator<DateString, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        val text = value?.filter { it.isDigit() } ?: return true
        val format = "yyyMMdd"
        return runCatching {
            text.toLocalDate().let {
                if (text != it.toString(format)) null else true
            }
        }.getOrNull() != null
    }
}