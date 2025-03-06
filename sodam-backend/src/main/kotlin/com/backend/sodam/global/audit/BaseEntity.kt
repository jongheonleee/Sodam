package com.backend.sodam.global.audit

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false, nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.MIN
        protected set

    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false, nullable = false)
    var createdBy: String = "system"
        protected set
}
