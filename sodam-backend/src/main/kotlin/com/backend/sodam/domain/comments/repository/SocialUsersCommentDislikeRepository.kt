package com.backend.sodam.domain.comments.repository

import com.backend.sodam.domain.comments.entity.UsersDislikeCommentEntity
import com.backend.sodam.domain.comments.service.port.CreateUserCommentDislikePort
import com.backend.sodam.domain.comments.service.port.DeleteUserCommentDislikePort
import com.backend.sodam.domain.comments.service.port.FetchUserCommentDislikePort
import com.backend.sodam.domain.comments.service.port.UpdateUserCommentDislikePort
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class SocialUsersCommentDislikeRepository(
    private val commentJpaRepository: CommentJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val usersCommentDislikeJpaRepository: UsersCommentDislikeJpaRepository
) : CreateUserCommentDislikePort, FetchUserCommentDislikePort, UpdateUserCommentDislikePort, DeleteUserCommentDislikePort {

    override fun isTarget(userType: UserType): Boolean =
        UserType.SOCIAL == userType

    @Transactional
    override fun createDislike(userId: String, commentId: Long) {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        val commentEntity = commentJpaRepository.findByCommentId(commentId).get()
        val userDislikeCommentEntity = UsersDislikeCommentEntity(
            socialUser = socialUserEntity,
            comment = commentEntity
        )
        usersCommentDislikeJpaRepository.save(userDislikeCommentEntity)
    }

    @Transactional(readOnly = true)
    override fun existsCommentDislike(commentId: Long, userId: String): Boolean {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        val commentEntity = commentJpaRepository.findByCommentId(commentId).get()
        return usersCommentDislikeJpaRepository.existsByCommentAndSocialUser(
            socialUser = socialUserEntity,
            comment = commentEntity
        )
    }

    @Transactional
    override fun deleteDislike(commentId: Long, userId: String) {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        val commentEntity = commentJpaRepository.findByCommentId(commentId).get()
        val commentDislikeEntity = usersCommentDislikeJpaRepository.findByCommentAndSocialUser(
            socialUser = socialUserEntity,
            comment = commentEntity
        ).get()
        usersCommentDislikeJpaRepository.save(commentDislikeEntity)
    }
}
