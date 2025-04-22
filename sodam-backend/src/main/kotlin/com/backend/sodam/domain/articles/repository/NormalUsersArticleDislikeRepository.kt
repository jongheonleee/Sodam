package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.entity.UsersDislikeArticleEntity
import com.backend.sodam.domain.articles.service.port.CreateUserArticleDislikePort
import com.backend.sodam.domain.articles.service.port.DeleteUserArticleDislikePort
import com.backend.sodam.domain.articles.service.port.FetchUserArticleDislikePort
import com.backend.sodam.domain.articles.service.port.UpdateUserArticleDislikePort
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class NormalUsersArticleDislikeRepository(
    private val articleJpaRepository: ArticleJpaRepository,
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val usersArticleDislikeJpaRepository: UsersArticleDislikeJpaRepository
) : CreateUserArticleDislikePort, FetchUserArticleDislikePort, UpdateUserArticleDislikePort, DeleteUserArticleDislikePort {

    override fun isTarget(userType: UserType): Boolean =
        UserType.NORMAL == userType

    @Transactional(readOnly = true)
    override fun existsArticleDislike(articleId: Long, userId: String): Boolean {
        val normalUserEntity = normalUserJpaRepository.findByUserId(userId).get()
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        return usersArticleDislikeJpaRepository.existsByArticleAndUser(article = articleEntity, user = normalUserEntity)
    }

    @Transactional
    override fun deleteDislike(articleId: Long, userId: String) {
        val normalUserEntity = normalUserJpaRepository.findByUserId(userId).get()
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val userDislikeArticleEntity = usersArticleDislikeJpaRepository.findByArticleAndUser(article = articleEntity, user = normalUserEntity).get()
        usersArticleDislikeJpaRepository.delete(userDislikeArticleEntity)
    }

    @Transactional
    override fun createDislike(userId: String, articleId: Long) {
        val normalUserEntity = normalUserJpaRepository.findByUserId(userId).get()
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val userArticleDislikeEntity = UsersDislikeArticleEntity(article = articleEntity, user = normalUserEntity)
        usersArticleDislikeJpaRepository.save(userArticleDislikeEntity)
    }
}
