package com.backend.sodam.global.audit

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class MutableBaseEntity : BaseEntity() {
    @LastModifiedDate
    @Column(name = "MODIFIED_AT", nullable = false)
    var modifiedAt: LocalDateTime = LocalDateTime.MIN
        protected set

    @LastModifiedBy
    @Column(name = "MODIFIED_BY")
    var modifiedBy: String = "system"
        protected set
}
