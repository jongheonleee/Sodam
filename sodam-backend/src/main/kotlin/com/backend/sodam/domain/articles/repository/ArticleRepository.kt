package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.model.SodamArticle
import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand
import com.backend.sodam.domain.categories.repository.CategoryJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserRepository
import com.backend.sodam.domain.users.repository.UserJpaRepository
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

        return articleJpaRepository.save(articleCreateRequestEntity)
                                   .toDomain();
    }

    @Transactional
    fun createArticleForUser(userId: String, articleCreateCommand: ArticleCreateCommand) : SodamArticle {
        val foundUserEntity = userJpaRepository.findByUserId(userId).get()
        val foundCategoryEntity = categoryJpaRepository.findByCategoryId(articleCreateCommand.categoryId).get()

        val articleCreateRequestEntity = articleCreateCommand.toEntity(
            userEntity = foundUserEntity,
            categoryEntity = foundCategoryEntity,
        )

        return articleJpaRepository.save(articleCreateRequestEntity)
            .toDomain();
    }
}