package com.backend.sodam.global.audit

import lombok.RequiredArgsConstructor
import org.springframework.context.ApplicationContext
import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.*

// 시스템 칼럼의 작성자 부분
@Component
@RequiredArgsConstructor
class RequestedByAuditorAware(
    val context: ApplicationContext
) : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        try {
            return Optional.of(context.getBean(RequestedByProvider::class.java))
                .flatMap { it.getRequestedBy() }
        } catch (e: Exception) {
            return Optional.of("system"); // 없으면 system으로 기재
        }
    }
}
