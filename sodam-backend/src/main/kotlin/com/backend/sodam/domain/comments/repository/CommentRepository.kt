package com.backend.sodam.domain.comments.repository

import com.backend.sodam.domain.articles.repository.ArticleJpaRepository
import com.backend.sodam.domain.comments.model.SodamComment
import com.backend.sodam.domain.comments.service.command.CommentCreateCommand
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
    private val formatter: Formatter,
) {

    @Transactional
    fun createCommentForSocialUser(articleId: Long, commentCreateCommand: CommentCreateCommand) : SodamComment {
        val foundArticleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val foundSocialUserEntity = socialUserJpaRepository.findBySocialUserId(commentCreateCommand.userId).get()

        val commentCreateEntity = commentCreateCommand.toEntity(
            articleEntity = foundArticleEntity,
            socialUsersEntity = foundSocialUserEntity,
        )

        val savedCommentEntity = commentJpaRepository.save(commentCreateEntity)


        return SodamComment(
            commentId = savedCommentEntity.commentId!!,
            articleId = savedCommentEntity.article!!.articleId!!,
            profileImageUrl = savedCommentEntity.userImage!!,
            userName = savedCommentEntity.socialUser!!.userName,
            createdAt = formatter.timeFormat(savedCommentEntity.createdAt),
            content = savedCommentEntity.commentContent,
            commentLikeCnt = savedCommentEntity.commentLikeCnt,
            commentDislikeCnt = savedCommentEntity.commentDislikeCnt,
        )
    }

    @Transactional
    fun createCommentForUser(articleId: Long, commentCreateCommand: CommentCreateCommand) : SodamComment {
        val foundArticleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val foundUserEntity = userJpaRepository.findByUserId(commentCreateCommand.userId).get()

        val commentCreateEntity = commentCreateCommand.toEntity(
            articleEntity = foundArticleEntity,
            userEntity = foundUserEntity,
        )

        val savedCommentEntity = commentJpaRepository.save(commentCreateEntity)

        return SodamComment(
            commentId = savedCommentEntity.commentId!!,
            articleId = savedCommentEntity.article!!.articleId!!,
            profileImageUrl = savedCommentEntity.userImage!!,
            userName = savedCommentEntity.socialUser!!.userName,
            createdAt = formatter.timeFormat(savedCommentEntity.createdAt),
            content = savedCommentEntity.commentContent,
            commentLikeCnt = savedCommentEntity.commentLikeCnt,
            commentDislikeCnt = savedCommentEntity.commentDislikeCnt,
        )
    }
}