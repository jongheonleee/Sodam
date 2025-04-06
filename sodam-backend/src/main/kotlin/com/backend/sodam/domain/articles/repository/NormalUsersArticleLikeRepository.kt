package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.entity.UsersLikeArticleEntity
import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.exception.UsersArticleLikeException
import com.backend.sodam.domain.articles.service.port.CreateUserArticleLikePort
import com.backend.sodam.domain.articles.service.port.DeleteUserArticleLikePort
import com.backend.sodam.domain.articles.service.port.FetchUserArticleLikePort
import com.backend.sodam.domain.articles.service.port.UpdateUserArticleLikePort
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class NormalUsersArticleLikeRepository(
    private val articleJpaRepository: ArticleJpaRepository,
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val usersArticleLikeJpaRepository: UsersArticleLikeJpaRepository
): CreateUserArticleLikePort, FetchUserArticleLikePort, UpdateUserArticleLikePort, DeleteUserArticleLikePort {

    override fun isTarget(userType: UserType): Boolean =
        UserType.NORMAL == userType

    @Transactional(readOnly = true)
    override fun existsArticleLike(articleId: Long, userId: String): Boolean {
        val normalUserEntity = normalUserJpaRepository.findByUserId(userId).get()
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        return usersArticleLikeJpaRepository.existsByArticleAndUser(users = normalUserEntity, article = articleEntity)
    }

    @Transactional
    override fun deleteLike(articleId: Long, userId: String) {
        val normalUserEntity = normalUserJpaRepository.findByUserId(userId).get()
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val userLikeArticleEntity = usersArticleLikeJpaRepository.findByArticleAndUser(article = articleEntity, users = normalUserEntity).get()
        usersArticleLikeJpaRepository.delete(userLikeArticleEntity)
    }

    @Transactional
    override fun createLike(userId: String, articleId: Long) {
        val normalUserEntity = normalUserJpaRepository.findByUserId(userId).get()
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val userLikeArticleEntity = UsersLikeArticleEntity(article = articleEntity, user = normalUserEntity)
        usersArticleLikeJpaRepository.save(userLikeArticleEntity)
    }
}
