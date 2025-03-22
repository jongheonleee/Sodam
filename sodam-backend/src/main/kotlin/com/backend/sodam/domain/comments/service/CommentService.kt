package com.backend.sodam.domain.comments.service

import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.repository.ArticleRepository
import com.backend.sodam.domain.comments.exception.CommentException
import com.backend.sodam.domain.comments.repository.CommentRepository
import com.backend.sodam.domain.comments.service.command.CommentCreateCommand
import com.backend.sodam.domain.comments.service.command.CommentUpdateCommand
import com.backend.sodam.domain.comments.service.response.CommentCreateResponse
import com.backend.sodam.domain.comments.service.response.CommentSimpleResponse
import com.backend.sodam.domain.comments.service.response.CommentUpdateResponse
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class CommentService(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {

    fun create(articleId: Long, commentCreateCommand: CommentCreateCommand): CommentCreateResponse {
        if (!articleRepository.isExistsByArticleId(articleId)) {
            throw ArticleException.ArticleNotFoundException()
        }

        val sodamUser = userRepository.findByUserId(commentCreateCommand.userId).get()
        val sodamComment = when (sodamUser.userType) {
            UserType.SOCIAL -> {
                commentCreateCommand.profileImageUrl = commentCreateCommand.profileImageUrl
                commentRepository.createCommentForSocialUser(
                    articleId = articleId,
                    commentCreateCommand = commentCreateCommand
                )
            }

            else -> {
                commentCreateCommand.profileImageUrl = commentCreateCommand.profileImageUrl
                commentRepository.createCommentForUser(
                    articleId = articleId,
                    commentCreateCommand = commentCreateCommand
                )
            }
        }

        return CommentCreateResponse(
            commentId = sodamComment.commentId,
            articleId = sodamComment.articleId,
            profileImageUrl = sodamComment.profileImageUrl,
            userName = sodamComment.userName,
            createdAt = sodamComment.createdAt,
            content = sodamComment.content,
            commentLikeCnt = sodamComment.commentLikeCnt,
            commentDislikeCnt = sodamComment.commentDislikeCnt
        )
    }

    fun update(commentId: Long, commentUpdateCommand: CommentUpdateCommand): CommentUpdateResponse {
        // 해당 댓글을 작성한 사용자 인지 확인한다
        // 그렇지 않다면, 권한 없음을 나타내는 예외를 발생시킨다.
        val sodamComment = commentRepository.findByCommentId(commentId)
        if (!sodamComment.canAccess(commentUpdateCommand.userId)) {
            throw CommentException.CommentAccessDeniedException()
        }

        val updatedSodamComment = commentRepository.update(commentId, commentUpdateCommand)
        // 만약 맞다면, 해당 댓글을 수정한다.
        // 수정된 내용을 리스폰스 객체로 담아서 반환한다.
        return CommentUpdateResponse(
            commentId = updatedSodamComment.commentId,
            comment = updatedSodamComment.content
        )
    }

    fun getSimpleComment(commentId: Long): CommentSimpleResponse {
        val sodamComment = commentRepository.findByCommentId(commentId)
        return CommentSimpleResponse(
            commentId = sodamComment.commentId,
            comment = sodamComment.content
        )
    }

    fun delete(userId: String, commentId: Long) {
        val sodamComment = commentRepository.findByCommentId(commentId)
        if (!sodamComment.canAccess(userId)) {
            throw CommentException.CommentAccessDeniedException()
        }

        commentRepository.delete(commentId)
    }
}
