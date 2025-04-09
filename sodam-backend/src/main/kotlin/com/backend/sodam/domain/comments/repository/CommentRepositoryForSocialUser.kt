package com.backend.sodam.domain.comments.repository

import com.backend.sodam.domain.articles.repository.ArticleJpaRepository
import com.backend.sodam.domain.comments.model.SodamComment
import com.backend.sodam.domain.comments.service.command.CommentCreateCommand
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.global.utils.Formatter
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class CommentRepositoryForSocialUser(
    private val articleJpaRepository: ArticleJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val userJpaRepository: NormalUserJpaRepository,
    private val commentJpaRepository: CommentJpaRepository,
    private val commentLikeJpaRepository: UsersCommentLikeJpaRepository,
    private val commentDislikeJpaRepository: UsersCommentDislikeJpaRepository,
    private val formatter: Formatter
): AbstractCommentRepository(articleJpaRepository, socialUserJpaRepository, userJpaRepository, commentJpaRepository, commentLikeJpaRepository, commentDislikeJpaRepository, formatter) {

    override fun isTarget(userType: UserType): Boolean =
        UserType.SOCIAL == userType

    @Transactional
    override fun createComment(articleId: Long, commentCreateCommand: CommentCreateCommand): SodamComment {
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(commentCreateCommand.userId).get()
        val commentCreateEntity = commentCreateCommand.toEntity(
            articleEntity = articleEntity,
            socialUsersEntity = socialUserEntity
        )
        return commentJpaRepository.save(commentCreateEntity)
                                   .toDomain()
    }
}
