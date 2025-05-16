package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand
import com.backend.sodam.domain.categories.repository.CategoryJpaRepository
import com.backend.sodam.domain.comments.repository.CommentRepositoryForNormalUser
import com.backend.sodam.domain.tags.entity.TagsEntity
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ArticleRepositoryForNormalUser(
    private val articleJpaRepository: ArticleJpaRepository,
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val categoryJpaRepository: CategoryJpaRepository,
    private val commentRepositoryForNormalUser: CommentRepositoryForNormalUser,
    private val articleLikeJpaRepository: UsersArticleLikeJpaRepository,
    private val articleDislikeJpaRepository: UsersArticleDislikeJpaRepository
) : AbstractArticleRepository(commentRepositoryForNormalUser, articleJpaRepository, categoryJpaRepository, articleLikeJpaRepository, articleDislikeJpaRepository) {

    override fun isTarget(userType: UserType): Boolean =
        UserType.NORMAL == userType

    @Transactional
    override fun createArticle(userId: String, articleCreateCommand: ArticleCreateCommand): SodamArticle {
        val normalUserEntity = normalUserJpaRepository.findByUserId(userId).get()
        val categoryEntity = categoryJpaRepository.findByCategoryId(articleCreateCommand.categoryId).get()
        val articleCreateRequestEntity = articleCreateCommand.toEntity(userEntity = normalUserEntity, categoryEntity = categoryEntity)

        articleCreateCommand.tags.map {
            articleCreateRequestEntity.addTag(
                TagsEntity(tagName = it)
            )
        }

        return articleJpaRepository.save(articleCreateRequestEntity)
            .toDomain()
    }
}
