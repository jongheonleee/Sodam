package com.backend.sodam.domain.comments.repository

import com.backend.sodam.domain.articles.repository.ArticleJpaRepository
import com.backend.sodam.domain.comments.exception.CommentException
import com.backend.sodam.domain.comments.model.SodamComment
import com.backend.sodam.domain.comments.service.command.CommentCreateCommand
import com.backend.sodam.domain.comments.service.command.CommentUpdateCommand
import com.backend.sodam.domain.comments.service.port.CreateCommentPort
import com.backend.sodam.domain.comments.service.port.DeleteCommentPort
import com.backend.sodam.domain.comments.service.port.FetchCommentPort
import com.backend.sodam.domain.comments.service.port.UpdateCommentPort
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.global.utils.Formatter
import org.springframework.transaction.annotation.Transactional


abstract class AbstractCommentRepository(
    private val articleJpaRepository: ArticleJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val userJpaRepository: NormalUserJpaRepository,
    private val commentJpaRepository: CommentJpaRepository,
    private val commentLikeJpaRepository: UsersCommentLikeJpaRepository,
    private val commentDislikeJpaRepository: UsersCommentDislikeJpaRepository,
    private val formatter: Formatter
): CreateCommentPort, FetchCommentPort, UpdateCommentPort, DeleteCommentPort {

    abstract override fun isTarget(userType: UserType): Boolean
    abstract override fun createComment(articleId: Long, commentCreateCommand: CommentCreateCommand): SodamComment


    @Transactional(readOnly = true)
    override fun findByCommentId(commentId: Long): SodamComment {
        val commentEntity = commentJpaRepository.findByCommentId(commentId).get()
        return commentEntity.toDomain()
    }

    @Transactional(readOnly = true)
    override fun isExistsComment(commentId: Long): Boolean = commentJpaRepository.existsByCommentId(commentId = commentId)

    @Transactional
    override fun update(commentId: Long, commentUpdateCommand: CommentUpdateCommand): SodamComment {
        val commentEntity = commentJpaRepository.findByCommentId(commentId).get()
        commentEntity.update(commentUpdateCommand = commentUpdateCommand)
        return commentJpaRepository.save(commentEntity)
                                   .toDomain()
    }

    @Transactional
    override fun delete(commentId: Long) {
        val commentEntity = commentJpaRepository.findByCommentId(commentId).get()
        val foundCommentLikeByComment = commentLikeJpaRepository.findByComment(commentEntity)
        commentLikeJpaRepository.deleteAll(foundCommentLikeByComment)
        val foundCommentDislikeByComment = commentDislikeJpaRepository.findByComment(commentEntity)
        commentDislikeJpaRepository.deleteAll(foundCommentDislikeByComment)
        commentJpaRepository.delete(commentEntity)
    }

    @Transactional
    override fun decreaseLikeCnt(commentId: Long) {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundCommentEntity = foundCommentEntityOptional.get()
        foundCommentEntity.decreaseLikeCnt()
    }

    @Transactional
    override fun increaseLikeCnt(commentId: Long) {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundCommentEntity = foundCommentEntityOptional.get()
        foundCommentEntity.increaseLikeCnt()
    }

    @Transactional
    override fun decreaseDislikeCnt(commentId: Long) {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundCommentEntity = foundCommentEntityOptional.get()
        foundCommentEntity.decreaseDislikeCnt()
    }

    @Transactional
    override fun increaseDislikeCnt(commentId: Long) {
        val foundCommentEntityOptional = commentJpaRepository.findByCommentId(commentId)
        if (foundCommentEntityOptional.isEmpty) {
            throw CommentException.CommentNotFoundException()
        }

        val foundCommentEntity = foundCommentEntityOptional.get()
        foundCommentEntity.increaseDislikeCnt()
    }
}
