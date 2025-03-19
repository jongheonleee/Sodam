package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.entity.ArticleEntity
import com.backend.sodam.domain.articles.entity.UsersDislikeArticleEntity
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UsersArticleDislikeJpaRepository : JpaRepository<UsersDislikeArticleEntity, Long> {
    fun existsByArticleAndSocialUser(article: ArticleEntity, socialUser: SocialUsersEntity) : Boolean
    fun existsByArticleAndUser(article: ArticleEntity, user: UsersEntity) : Boolean
    fun findByArticleAndSocialUser(article: ArticleEntity, socialUser: SocialUsersEntity) : Optional<UsersDislikeArticleEntity>
    fun findByArticleAndUser(article: ArticleEntity, user: UsersEntity) : Optional<UsersDislikeArticleEntity>
}