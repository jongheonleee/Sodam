package com.backend.sodam.global.utils

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class Formatter {

    companion object {
        val timeFormatterPattern: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss")
    }

    fun timeFormat(localDateTime: LocalDateTime): String {
        return localDateTime.format(timeFormatterPattern)
    }
}