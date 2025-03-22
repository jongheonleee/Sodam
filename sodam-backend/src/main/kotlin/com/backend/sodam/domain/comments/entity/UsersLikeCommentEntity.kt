package com.backend.sodam.domain.comments.entity

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
@Table(name = "user_like_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersLikeCommentEntity(
    // PK 및 불변 필드
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USERS_LIKE_COMMENT_ID")
    val commentLikeId: Long? = null,

    // FK(추후에 연관관계 매핑)
    // - 댓글 아이디 : 회원 싫어요 댓글 - 댓글 = N : 1 ✅
    // - 회원 아이디 : 회원 싫어요 댓글 - 회원 = N : 1 ✅
    @ManyToOne
    @JoinColumn(name = "COMMENT_ID")
    val comment: CommentEntity? = null,

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    val user: UsersEntity? = null,

    @ManyToOne
    @JoinColumn(name = "SOCIAL_USER_ID")
    val socialUser: SocialUsersEntity? = null
) : MutableBaseEntity()
