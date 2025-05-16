package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand
import com.backend.sodam.domain.categories.repository.CategoryJpaRepository
import com.backend.sodam.domain.comments.repository.CommentRepositoryForNormalUser
import com.backend.sodam.domain.tags.entity.TagsEntity
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ArticleRepositoryForSocialUser(
    private val articleJpaRepository: ArticleJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val categoryJpaRepository: CategoryJpaRepository,
    private val commentRepositoryForNormalUser: CommentRepositoryForNormalUser,
    private val articleLikeJpaRepository: UsersArticleLikeJpaRepository,
    private val articleDislikeJpaRepository: UsersArticleDislikeJpaRepository
) : AbstractArticleRepository(commentRepositoryForNormalUser, articleJpaRepository, categoryJpaRepository, articleLikeJpaRepository, articleDislikeJpaRepository) {

    override fun isTarget(userType: UserType): Boolean =
        UserType.SOCIAL == userType

    @Transactional
    override fun createArticle(userId: String, articleCreateCommand: ArticleCreateCommand): SodamArticle {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        val categoryEntity = categoryJpaRepository.findByCategoryId(articleCreateCommand.categoryId).get()
        val articleCreateRequestEntity = articleCreateCommand.toEntity(socialUsersEntity = socialUserEntity, categoryEntity = categoryEntity)

        articleCreateCommand.tags.map {
            val tagEntity = TagsEntity(tagName = it)
            articleCreateRequestEntity.addTag(tagEntity)
        }

        return articleJpaRepository.save(articleCreateRequestEntity)
            .toDomain()
    }
}
