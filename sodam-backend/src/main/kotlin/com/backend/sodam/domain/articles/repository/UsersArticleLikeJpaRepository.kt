package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.entity.ArticleEntity
import com.backend.sodam.domain.articles.entity.UsersLikeArticleEntity
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UsersArticleLikeJpaRepository : JpaRepository<UsersLikeArticleEntity, Long> {
    fun existsByArticleAndUser(article: ArticleEntity, users: UsersEntity) : Boolean
    fun existsByArticleAndSocialUser(article: ArticleEntity, socialUser: SocialUsersEntity) : Boolean
    fun findByArticleAndUser(article: ArticleEntity, users: UsersEntity) : Optional<UsersLikeArticleEntity>
    fun findByArticleAndSocialUser(article: ArticleEntity, socialUser: SocialUsersEntity) : Optional<UsersLikeArticleEntity>
}