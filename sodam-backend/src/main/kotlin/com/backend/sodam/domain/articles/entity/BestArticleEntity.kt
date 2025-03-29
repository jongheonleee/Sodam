package com.backend.sodam.domain.articles.entity

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
import java.time.LocalDateTime

@Entity
@Table(name = "best_articles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class BestArticleEntity(
    // PK 및 불변 필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BEST_ARTICLE_ID")
    val bestArticleId: Long? = null,

    // FK(추후에 연관관계 매핑)
    // - 게시글 아이디 : 게시글 : 베스트 게시글 = 1 : N ✅
    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    var article: ArticleEntity? = null, // 게시글은 불변

    @Column(name = "ARTICLE_TITLE")
    val articleTitle: String,

    @Column(name = "ARTICLE_SUMMARY")
    val articleSummary: String,

    @Column(name = "ARTICLE_AUTHOR")
    val articleAuthor: String,

    @Column(name = "TAG1")
    val tag1: String,

    @Column(name = "TAG2")
    val tag2: String,

    @Column(name = "TAG3")
    val tag3: String,


    // 가변 필드
    startAt: LocalDateTime,
    endAt: LocalDateTime
) : MutableBaseEntity() {

    @Column(name = "START_AT")
    var startAt = startAt
        protected set

    @Column(name = "END_AT")
    var endAt = endAt
        protected set
}
