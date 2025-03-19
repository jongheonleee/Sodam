package com.backend.sodam.domain.comments.service

import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.repository.ArticleRepository
import com.backend.sodam.domain.comments.repository.CommentRepository
import com.backend.sodam.domain.comments.service.command.CommentCreateCommand
import com.backend.sodam.domain.comments.service.response.CommentCreateResponse
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class CommentService(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
) {

    fun create(articleId: Long, commentCreateCommand: CommentCreateCommand) : CommentCreateResponse {
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
            commentDislikeCnt = sodamComment.commentDislikeCnt,
        )

    }
}