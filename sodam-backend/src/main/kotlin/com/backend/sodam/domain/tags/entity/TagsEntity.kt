package com.backend.sodam.domain.tags.entity

import com.backend.sodam.domain.articles.entity.ArticleEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class TagsEntity(
    // PK 및 불변 필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    val tagId: Long,

    // FK(추후에 연관관계 매핑)
    // - 게시글 아이디 : 태그 - 게시글 = N : 1
    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    val article: ArticleEntity,

    // 가변 필드
    tagName: String

) : MutableBaseEntity() {

    @Column(name = "TAG_NAME")
    var tagName = tagName
        protected set
}
