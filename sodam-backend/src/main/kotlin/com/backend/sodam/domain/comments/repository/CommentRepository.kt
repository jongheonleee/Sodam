package com.backend.sodam.domain.comments.repository

import com.backend.sodam.domain.articles.repository.ArticleJpaRepository
import com.backend.sodam.domain.comments.exception.CommentException
import com.backend.sodam.domain.comments.model.SodamComment
import com.backend.sodam.domain.comments.service.command.CommentCreateCommand
import com.backend.sodam.domain.comments.service.command.CommentUpdateCommand
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.repository.UserJpaRepository
import com.backend.sodam.global.utils.Formatter
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class CommentRepository(
    private val articleJpaRepository: ArticleJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val userJpaRepository: UserJpaRepository,
    private val commentJpaRepository: CommentJpaRepository,
    private val commentLikeJpaRepository: UsersCommentLikeJpaRepository,
    private val commentDislikeJpaRepository: UsersCommentDislikeJpaRepository,
    private val formatter: Formatter
) {

    @Transactional
    fun createCommentForSocialUser(articleId: Long, commentCreateCommand: CommentCreateCommand): SodamComment {
        val foundArticleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val foundSocialUserEntity = socialUserJpaRepository.findBySocialUserId(commentCreateCommand.userId).get()

        val commentCreateEntity = commentCreateCommand.toEntity(
            articleEntity = foundArticleEntity,
            socialUsersEntity = foundSocialUserEntity
        )

        return commentJpaRepository.save(commentCreateEntity)
            .toDomain()
    }

    @Transactional(readOnly = true)
    fun findByCommentId(commentId: Long): SodamComment {
        val foundCommentEntityOptionalByCommentId = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptionalByCommentId.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        return foundCommentEntityOptionalByCommentId.get()
            .toDomain()
    }

    @Transactional
    fun createCommentForUser(articleId: Long, commentCreateCommand: CommentCreateCommand): SodamComment {
        val foundArticleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val foundUserEntity = userJpaRepository.findByUserId(commentCreateCommand.userId).get()

        val commentCreateEntity = commentCreateCommand.toEntity(
            articleEntity = foundArticleEntity,
            userEntity = foundUserEntity
        )

        return commentJpaRepository.save(commentCreateEntity)
            .toDomain()
    }

    @Transactional
    fun update(commentId: Long, commentUpdateCommand: CommentUpdateCommand): SodamComment {
        val foundCommentEntityOptionalByCommentId = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptionalByCommentId.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundCommentEntity = foundCommentEntityOptionalByCommentId.get()
        foundCommentEntity.update(
            commentUpdateCommand = commentUpdateCommand
        )

        return commentJpaRepository.save(foundCommentEntity)
            .toDomain()
    }

    @Transactional
    fun delete(commentId: Long) {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundCommentEntity = foundCommentEntityOptional.get()
        val foundCommentLikeByComment = commentLikeJpaRepository.findByComment(foundCommentEntity)
        commentLikeJpaRepository.deleteAll(foundCommentLikeByComment)
        val foundCommentDislikeByComment = commentDislikeJpaRepository.findByComment(foundCommentEntity)
        commentDislikeJpaRepository.deleteAll(foundCommentDislikeByComment)
        commentJpaRepository.delete(foundCommentEntity)
    }

    @Transactional
    fun decreaseLikeCnt(commentId: Long) {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundCommentEntity = foundCommentEntityOptional.get()
        foundCommentEntity.decreaseLikeCnt()
    }

    @Transactional
    fun increaseLikeCnt(commentId: Long) {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundCommentEntity = foundCommentEntityOptional.get()
        foundCommentEntity.increaseLikeCnt()
    }

    @Transactional
    fun decreaseDislikeCnt(commentId: Long) {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundCommentEntity = foundCommentEntityOptional.get()
        foundCommentEntity.decreaseDislikeCnt()
    }

    @Transactional
    fun increaseDislikeCnt(commentId: Long) {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundCommentEntity = foundCommentEntityOptional.get()
        foundCommentEntity.increaseDislikeCnt()
    }
}
