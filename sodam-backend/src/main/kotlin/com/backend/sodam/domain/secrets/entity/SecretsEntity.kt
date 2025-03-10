package com.backend.sodam.domain.secrets.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.util.UUID

@Entity
@Table(name = "secretes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SecretsEntity(
    // pk 및 불변 필드
    @Id
    @Column(name = "SECRETE_ID")
    val secretId: UUID,

    @Column(name = "SECRETE_AUTHOR")
    val secretAuthor: String,

    // 가변 필드
    secretTitle: String,
    secretContent: String
) : MutableBaseEntity() {

    @Column(name = "SECRETE_TITLE")
    var secretTitle = secretTitle
        protected set

    @Column(name = "SECRETE_CONTENT")
    var secretContent = secretContent
        protected set
}
