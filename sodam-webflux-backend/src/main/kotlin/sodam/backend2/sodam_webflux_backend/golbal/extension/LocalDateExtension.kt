package sodam.backend2.sodam_webflux_backend.golbal.extension

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.toLocalDate(format: String = "yyyy-MM-dd"): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofPattern(format))
}

fun LocalDate.toString(format: String): String {
    return this.format(DateTimeFormatter.ofPattern(format))
}