package com.backend.sodam.domain.comments.entity

import com.backend.sodam.domain.articles.entity.ArticleEntity
import com.backend.sodam.domain.users.entity.SocialUsersEntity
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
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class CommentEntity(
    // PK 및 불변 필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    val commentId: Long? = null,

    @Column(name = "USER_IMAGE")
    val userImage: String? = null,

    // FK(추후에 연관관계 매핑)
    // - 게시글 아이디 : 댓글 - 게시글 = N : 1 ✅
    // - 회원 아이디 : 댓글 - 회원 = N : 1 ✅
    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    val article: ArticleEntity? = null,

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user: UsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SOCIAL_USER_ID")
    val socialUser: SocialUsersEntity? = null,

    // 가변 필드
    commentContent: String,
    commentLikeCnt: Long,
    commentDislikeCnt: Long
) : MutableBaseEntity() {

    @Column(name = "COMMENT_CONTENT")
    var commentContent = commentContent
        protected set

    @Column(name = "COMMENT_LIKE_CNT")
    var commentLikeCnt = commentLikeCnt
        protected set

    @Column(name = "COMMENT_DISLIKE_CNT")
    var commentDislikeCnt = commentDislikeCnt
        protected set
}
