package com.backend.sodam.domain.comments.repository

import com.backend.sodam.domain.comments.entity.UsersDislikeCommentEntity
import com.backend.sodam.domain.comments.exception.CommentException
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.repository.UserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class UsersCommentDislikeRepository(
    private val userJpaRepository: UserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val commentJpaRepository: CommentJpaRepository,
    private val usersCommentDislikeJpaRepository: UsersCommentDislikeJpaRepository
) {

    @Transactional(readOnly = true)
    fun existsByCommentDislikeForSocialUser(commentId: Long, socialUserId: String): Boolean {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundSocialUserEntityOptional = socialUserJpaRepository.findBySocialUserId(socialUserId)
        if (foundSocialUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        return usersCommentDislikeJpaRepository.existsByCommentAndSocialUser(
            socialUser = foundSocialUserEntityOptional.get(),
            comment = foundCommentEntityOptional.get()
        )
    }

    @Transactional(readOnly = true)
    fun existsByCommentDislikeForUser(commentId: Long, userId: String): Boolean {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundUserEntityOptional = userJpaRepository.findByUserId(userId)
        if (foundUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        return usersCommentDislikeJpaRepository.existsByCommentAndUser(
            user = foundUserEntityOptional.get(),
            comment = foundCommentEntityOptional.get()
        )
    }

    @Transactional
    fun deleteForSocialUser(commentId: Long, socialUserId: String) {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundSocialUserEntityOptional = socialUserJpaRepository.findBySocialUserId(socialUserId)
        if (foundSocialUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundCommentDislikeEntityOptional = usersCommentDislikeJpaRepository.findByCommentAndSocialUser(
            socialUser = foundSocialUserEntityOptional.get(),
            comment = foundCommentEntityOptional.get()
        )

        if (foundCommentDislikeEntityOptional.isEmpty) {
            throw CommentException.UserDislikeCommentNotFoundException()
        }

        val foundCommentDislikeEntity = foundCommentDislikeEntityOptional.get()
        usersCommentDislikeJpaRepository.delete(foundCommentDislikeEntity)
    }

    @Transactional
    fun deleteForUser(commentId: Long, userId: String) {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundUserEntityOptional = userJpaRepository.findByUserId(userId)
        if (foundUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundCommentDislikeEntityOptional = usersCommentDislikeJpaRepository.findByCommentAndUser(
            comment = foundCommentEntityOptional.get(),
            user = foundUserEntityOptional.get()
        )

        if (foundCommentDislikeEntityOptional.isEmpty) {
            throw CommentException.UserDislikeCommentNotFoundException()
        }

        val foundCommentDislikeEntity = foundCommentDislikeEntityOptional.get()
        usersCommentDislikeJpaRepository.delete(foundCommentDislikeEntity)
    }

    @Transactional
    fun createForSocialUser(commentId: Long, socialUserId: String) {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundSocialUserEntityOptional = socialUserJpaRepository.findBySocialUserId(socialUserId)
        if (foundSocialUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val usersCommentDislikeEntity = UsersDislikeCommentEntity(
            comment = foundCommentEntityOptional.get(),
            socialUser = foundSocialUserEntityOptional.get()
        )

        usersCommentDislikeJpaRepository.save(usersCommentDislikeEntity)
    }

    @Transactional
    fun createForUser(commentId: Long, userId: String) {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundUserEntityOptional = userJpaRepository.findByUserId(userId)
        if (foundUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val usersCommentDislikeEntity = UsersDislikeCommentEntity(
            comment = foundCommentEntityOptional.get(),
            user = foundUserEntityOptional.get()
        )

        usersCommentDislikeJpaRepository.save(usersCommentDislikeEntity)
    }
}
