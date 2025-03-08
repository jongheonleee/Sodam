package com.backend.sodam.domain.comments.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "user_like_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class UsersLikeCommentEntity(
    // PK 및 불변 필드
    @Id
    @GeneratedValue
    @Column(name = "USERS_DISLIKE_COMMENT_ID")
    val likeCommentId: Long

    // FK(추후에 연관관계 매핑)
    // - 댓글 아이디 : 회원 좋아요 댓글 - 댓글 = N : 1
    // - 회원 아이디 : 회원 좋아요 댓글 - 회원 = N : 1
) : MutableBaseEntity()
