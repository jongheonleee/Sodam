package com.backend.sodam.domain.comments.entity

import com.backend.sodam.domain.articles.entity.ArticleEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "user_dislike_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersDislikeCommentEntity(
    // PK 및 불변 필드
    @Id
    @GeneratedValue
    @Column(name = "USERS_DISLIKE_COMMENT_ID")
    val dislikeCommentId: Long,

    // FK(추후에 연관관계 매핑)
    // - 댓글 아이디 : 회원 싫어요 댓글 - 댓글 = N : 1 ✅
    // - 회원 아이디 : 회원 싫어요 댓글 - 회원 = N : 1 ✅
    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    val comment : CommentEntity,

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user : UsersEntity,

    ) : MutableBaseEntity()
