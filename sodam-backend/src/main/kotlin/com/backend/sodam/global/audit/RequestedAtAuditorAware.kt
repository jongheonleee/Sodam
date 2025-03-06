package com.backend.sodam.global.audit

import org.springframework.data.auditing.DateTimeProvider
import org.springframework.stereotype.Component
import java.time.ZonedDateTime
import java.time.temporal.TemporalAccessor
import java.util.*

@Component
class RequestedAtAuditorAware : DateTimeProvider {

    override fun getNow(): Optional<TemporalAccessor> {
        return Optional.of(ZonedDateTime.now()) // 현재시간으로 설정
    }
}
