package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.controller.request.ArticleSearchRequest
import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.articles.model.SodamDetailArticle
import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand
import com.backend.sodam.domain.articles.service.command.ArticleSearchCommand
import com.backend.sodam.domain.categories.repository.CategoryJpaRepository
import com.backend.sodam.domain.tags.entity.TagsEntity
import com.backend.sodam.domain.tags.repository.TagJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.repository.UserJpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ArticleRepository(
    private val articleJpaRepository: ArticleJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val userJpaRepository: UserJpaRepository,
    private val categoryJpaRepository: CategoryJpaRepository,
) {

    @Transactional
    fun createArticleForSocialUser(userId: String, articleCreateCommand: ArticleCreateCommand) : SodamArticle {
        val foundSocialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        val foundCategoryEntity = categoryJpaRepository.findByCategoryId(articleCreateCommand.categoryId).get()

        val articleCreateRequestEntity = articleCreateCommand.toEntity(
            socialUsersEntity = foundSocialUserEntity,
            categoryEntity = foundCategoryEntity,
        )

        articleCreateCommand.tags.map {
            val tagEntity = TagsEntity(tagName = it)
            articleCreateRequestEntity.addTag(tagEntity)
        }

        return articleJpaRepository.save(articleCreateRequestEntity)
                                   .toDomain()
    }

    @Transactional
    fun createArticleForUser(userId: String, articleCreateCommand: ArticleCreateCommand) : SodamArticle {
        val foundUserEntity = userJpaRepository.findByUserId(userId).get()
        val foundCategoryEntity = categoryJpaRepository.findByCategoryId(articleCreateCommand.categoryId).get()

        val articleCreateRequestEntity = articleCreateCommand.toEntity(
            userEntity = foundUserEntity,
            categoryEntity = foundCategoryEntity,
        )

        articleCreateCommand.tags.map {
            val tagEntity = TagsEntity(tagName = it)
            articleCreateRequestEntity.addTag(tagEntity)
        }

        return articleJpaRepository.save(articleCreateRequestEntity)
                                   .toDomain()
    }

    @Transactional(readOnly = true)
    fun findByPageBy(pageRequest: Pageable, articleSearchCommand: ArticleSearchCommand): Page<SodamArticle> = articleJpaRepository.findByPageBy(
            pageRequest = pageRequest,
            articleSearchCommand = articleSearchCommand
    )

    @Transactional
    fun increaseViewCnt(articleId: Long) {
        val foundArticleOptionalByArticleId = articleJpaRepository.findByArticleId(articleId)

        if (foundArticleOptionalByArticleId.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }
        val foundArticleEntity = foundArticleOptionalByArticleId.get()
        foundArticleEntity.increaseViewCnt()
    }

    @Transactional(readOnly = true)
    fun findDetailByArticleId(articleId: Long) : SodamDetailArticle = articleJpaRepository.findDetailByArticleId(articleId)

    @Transactional(readOnly = true)
    fun isExistsByArticleId(articleId: Long) : Boolean = articleJpaRepository.findByArticleId(articleId).isPresent
}