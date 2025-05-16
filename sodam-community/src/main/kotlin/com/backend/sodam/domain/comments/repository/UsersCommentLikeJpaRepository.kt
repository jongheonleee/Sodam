package com.backend.sodam.domain.comments.repository

import com.backend.sodam.domain.comments.entity.CommentEntity
import com.backend.sodam.domain.comments.entity.UsersLikeCommentEntity
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UsersCommentLikeJpaRepository : JpaRepository<UsersLikeCommentEntity, Long> {
    fun existsByCommentAndSocialUser(comment: CommentEntity, socialUser: SocialUsersEntity): Boolean
    fun existsByCommentAndUser(comment: CommentEntity, user: UsersEntity): Boolean
    fun findByCommentAndSocialUser(comment: CommentEntity, socialUser: SocialUsersEntity): Optional<UsersLikeCommentEntity>
    fun findByCommentAndUser(comment: CommentEntity, user: UsersEntity): Optional<UsersLikeCommentEntity>
    fun findByComment(comment: CommentEntity): List<UsersLikeCommentEntity>
}
