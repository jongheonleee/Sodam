package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.entity.UsersDislikeArticleEntity
import com.backend.sodam.domain.articles.service.port.CreateUserArticleDislikePort
import com.backend.sodam.domain.articles.service.port.DeleteUserArticleDislikePort
import com.backend.sodam.domain.articles.service.port.FetchUserArticleDislikePort
import com.backend.sodam.domain.articles.service.port.UpdateUserArticleDislikePort
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class SocialUsersArticleDislikeRepository(
    private val articleJpaRepository: ArticleJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val usersArticleDislikeJpaRepository: UsersArticleDislikeJpaRepository
) : CreateUserArticleDislikePort, FetchUserArticleDislikePort, UpdateUserArticleDislikePort, DeleteUserArticleDislikePort {

    override fun isTarget(userType: UserType): Boolean =
        UserType.SOCIAL == userType

    @Transactional(readOnly = true)
    override fun existsArticleDislike(articleId: Long, userId: String): Boolean {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(socialUserId = userId).get()
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        return usersArticleDislikeJpaRepository.existsByArticleAndSocialUser(article = articleEntity, socialUser = socialUserEntity)
    }

    @Transactional
    override fun deleteDislike(articleId: Long, userId: String) {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(socialUserId = userId).get()
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val userDislikeArticleEntity = usersArticleDislikeJpaRepository.findByArticleAndSocialUser(article = articleEntity, socialUser = socialUserEntity).get()
        usersArticleDislikeJpaRepository.delete(userDislikeArticleEntity)
    }

    @Transactional
    override fun createDislike(userId: String, articleId: Long) {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(socialUserId = userId).get()
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val userArticleDislikeEntity = UsersDislikeArticleEntity(article = articleEntity, socialUser = socialUserEntity)
        usersArticleDislikeJpaRepository.save(userArticleDislikeEntity)
    }
}
