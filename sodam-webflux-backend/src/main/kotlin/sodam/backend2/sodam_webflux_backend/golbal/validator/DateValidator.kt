package sodam.backend2.sodam_webflux_backend.golbal.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import sodam.backend2.sodam_webflux_backend.golbal.annotation.DateString
import sodam.backend2.sodam_webflux_backend.golbal.extension.toLocalDate
import sodam.backend2.sodam_webflux_backend.golbal.extension.toString
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateValidator: ConstraintValidator<DateString, String> {

    // 2025-05-08 -> 20250508
    // [yyyyMMdd] 형식인지 검증
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        val text = value?.filter { it.isDigit() } ?: return true
        val format = "yyyMMdd"

        return runCatching {
            text.toLocalDate(format).let {
                if (text != it.toString(format)) null else true // 20000231 -> 포맷팅  -> 200000229로 나오는 경우가 있음. 이런 경우도 잘못된 데이터 들어온 경우임
            }
        }.getOrNull() != null
    }
}