package com.backend.sodam.domain.articles.entity
import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.articles.service.command.ArticleUpdateCommand
import com.backend.sodam.domain.categories.entity.CategoryEntity
import com.backend.sodam.domain.comments.entity.CommentEntity
import com.backend.sodam.domain.tags.entity.TagsEntity
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "articles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ArticleEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARTICLE_ID")
    val articleId: Long? = null,

    @Column(name = "USER_NAME")
    val name: String,

    // FK(추후에 연관관계 매핑)
    // - 카테고리 아이디 : 게시글 - 카테고리 = 1 : N ✅
    // - 회원 아이디 : 회원 - 게시글 = 1 : N ✅
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    var category: CategoryEntity, // 카테고리는 가변


    // 소셜 유저, 일반 유저
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = true)
    val user: UsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SOCIAL_USER_ID", nullable = true)
    val socialUser: SocialUsersEntity? = null,

    // 양방향 매핑 처리
    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true)
    var tags : MutableList<TagsEntity> = mutableListOf(),

    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments : MutableList<CommentEntity> = mutableListOf(),

    // 가변 필드
    articleTitle: String,
    articleSummary: String,
    articleContent: String,
    articleViewCnt: Long,
    articleLikeCnt: Long,
    articleDislikeCnt: Long
) : MutableBaseEntity() {

    @Column(name = "ARTICLE_TITLE")
    var articleTitle = articleTitle
        protected set

    @Column(name = "ARTICLE_SUMMARY")
    var articleSummary = articleSummary
        protected set

    @Column(name = "ARTICLE_CONTENT", columnDefinition = "TEXT")
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

    fun toDomain() : SodamArticle {
        return SodamArticle(
            userId = if (user != null) user!!.userId
                     else socialUser!!.socialUserId,
            articleId = articleId!!,
            title = articleTitle,
            author = name,
            summary = articleSummary,
            content = articleContent,
            tags = tags.map { it.tagName },
            viewCnt = articleViewCnt,
            likeCnt = articleLikeCnt,
            dislikeCnt = articleDislikeCnt,
            createdAt = createdAt.toString(),
        )
    }

    fun addTag(tag: TagsEntity) {
        tags.add(tag)
        tag.article = this
    }

    fun update(articleUpdateCommand: ArticleUpdateCommand, categoryEntity: CategoryEntity) {
        this.category = categoryEntity
        this.articleTitle = articleUpdateCommand.articleTitle
        this.articleSummary = articleUpdateCommand.articleSummary
        this.articleContent = articleUpdateCommand.articleContent
    }

    fun increaseViewCnt() {
        this.articleViewCnt++
    }
    
}
