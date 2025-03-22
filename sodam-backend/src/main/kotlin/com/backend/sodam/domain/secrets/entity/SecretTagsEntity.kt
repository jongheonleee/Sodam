package com.backend.sodam.domain.secrets.entity

import com.backend.sodam.domain.articles.entity.ArticleEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.NoArgsConstructor


@Entity
@Table(name = "secret_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SecretTagsEntity(
    // PK 및 불변 필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    val tagId: Long? = null,

    // FK(추후에 연관관계 매핑)
    // - 게시글 아이디 : 태그 - 게시글 = N : 1
    @ManyToOne
    @JoinColumn(name = "SECRETE_ID")
    var secret: SecretsEntity? = null,

    // 가변 필드
    tagName: String
) : MutableBaseEntity() {

    @Column(name = "TAG_NAME")
    var tagName = tagName
        protected set
}