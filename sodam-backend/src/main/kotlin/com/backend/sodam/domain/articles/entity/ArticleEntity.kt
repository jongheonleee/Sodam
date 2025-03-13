package com.backend.sodam.domain.articles.entity
import com.backend.sodam.domain.categories.entity.CategoryEntity
import com.backend.sodam.domain.users.entity.UsersEntity
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
@Table(name = "articles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ArticleEntity(

    // PK 및 불변 필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARTICLE_ID")
    val articleId: Long,

    @Column(name = "USER_EMAIL")
    val userEmail: String,

    // FK(추후에 연관관계 매핑)
    // - 카테고리 아이디 : 게시글 - 카테고리 = 1 : N ✅
    // - 회원 아이디 : 회원 - 게시글 = 1 : N ✅
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    var category: CategoryEntity, // 카테고리는 가변

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user: UsersEntity, // 회원은 불변

    // 가변 필드
    articleTitle: String,
    articleSummary: String,
    articleContent: String,
    articleViewCnt: Int,
    articleLikeCnt: Int,
    articleDislikeCnt: Int
) : MutableBaseEntity() {

    @Column(name = "ARTICLE_TITLE")
    var articleTitle = articleTitle
        protected set

    @Column(name = "ARTICLE_SUMMARY")
    var articleSummary = articleSummary
        protected set

    @Column(name = "ARTICLE_CONTENT")
    var articleContent = articleContent
        protected set

    @Column(name = "ARTICLE_VIEW_CNT")
    var articleViewCnt = articleViewCnt
        protected set

    @Column(name = "ARTICLE_LIKE_CNT")
    var articleLikeCnt = articleLikeCnt
        protected set

    @Column(name = "ARTICLE_DISLIKE_CNT")
    var articleDislikeCnt = articleDislikeCnt
        protected set
}
