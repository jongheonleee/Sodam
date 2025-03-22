package com.backend.sodam.domain.secrets.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "secretes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SecretsEntity(
    // pk 및 불변 필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SECRETE_ID")
    val secretId: Long? = null,

    @Column(name = "SECRETE_AUTHOR")
    val secretAuthor: String,

    // 양방향 매핑 처리
    @OneToMany(mappedBy = "secret", cascade = [CascadeType.ALL], orphanRemoval = true)
    var tags: MutableList<SecretTagsEntity> = mutableListOf(),

    // 가변 필드
    secretThumbnailUrl: String,
    secretTitle: String,
    secretContent: String
) : MutableBaseEntity() {

    @Column(name = "SECRETE_THUMBNAIL_URL")
    var secretThumbnailUrl = secretThumbnailUrl
        protected set

    @Column(name = "SECRETE_TITLE")
    var secretTitle = secretTitle
        protected set

    @Column(name = "SECRETE_CONTENT")
    var secretContent = secretContent
        protected set
}
