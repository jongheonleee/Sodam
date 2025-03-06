package com.backend.sodam.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

// persistance-layer의 JpaAuditConfig 설정 클래스
@Configuration
@EnableJpaAuditing(
    auditorAwareRef = "requestedByAuditorAware",
    dateTimeProviderRef = "requestedAtAuditorAware"
)
class JpaAuditConfig
