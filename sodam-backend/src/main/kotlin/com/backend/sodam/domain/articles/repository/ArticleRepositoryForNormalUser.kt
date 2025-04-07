package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.articles.model.SodamDetailArticle
import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand
import com.backend.sodam.domain.articles.service.command.ArticleSearchCommand
import com.backend.sodam.domain.articles.service.command.ArticleUpdateCommand
import com.backend.sodam.domain.articles.service.port.CreateArticlePort
import com.backend.sodam.domain.articles.service.port.DeleteArticlePort
import com.backend.sodam.domain.articles.service.port.FetchArticlePort
import com.backend.sodam.domain.articles.service.port.UpdateArticlePort
import com.backend.sodam.domain.categories.exception.CategoryException
import com.backend.sodam.domain.categories.repository.CategoryJpaRepository
import com.backend.sodam.domain.comments.repository.CommentJpaRepository
import com.backend.sodam.domain.comments.repository.CommentRepository
import com.backend.sodam.domain.tags.entity.TagsEntity
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ArticleRepositoryForNormalUser(
    private val articleJpaRepository: ArticleJpaRepository,
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val categoryJpaRepository: CategoryJpaRepository,
    private val commentRepository: CommentRepository,
    private val articleLikeJpaRepository: UsersArticleLikeJpaRepository,
    private val articleDislikeJpaRepository: UsersArticleDislikeJpaRepository
): AbstractArticleRepository(commentRepository,articleJpaRepository, categoryJpaRepository, articleLikeJpaRepository, articleDislikeJpaRepository) {

    override fun isTarget(userType: UserType): Boolean =
        UserType.NORMAL == userType

    @Transactional
    override fun createArticle(userId: String, articleCreateCommand: ArticleCreateCommand): SodamArticle {
        val normalUserEntity = normalUserJpaRepository.findByUserId(userId).get()
        val categoryEntity =  categoryJpaRepository.findByCategoryId(articleCreateCommand.categoryId).get()
        val articleCreateRequestEntity = articleCreateCommand.toEntity(userEntity = normalUserEntity, categoryEntity = categoryEntity)

        articleCreateCommand.tags.map {
            val tagEntity = TagsEntity(tagName = it)
            articleCreateRequestEntity.addTag(tagEntity)
        }

        return articleJpaRepository.save(articleCreateRequestEntity)
                                   .toDomain()
    }

}
