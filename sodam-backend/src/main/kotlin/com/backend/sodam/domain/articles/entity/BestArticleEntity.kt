package com.backend.sodam.domain.articles.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.*
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
    val bestArticleId: Long,

    // FK(추후에 연관관계 매핑)
    // - 게시글 아이디 : 게시글 : 베스트 게시글 = 1 : N ✅
    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    val article : ArticleEntity, // 게시글은 불변

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
