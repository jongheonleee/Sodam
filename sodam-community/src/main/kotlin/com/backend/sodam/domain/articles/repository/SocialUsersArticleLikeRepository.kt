package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.entity.UsersLikeArticleEntity
import com.backend.sodam.domain.articles.service.port.CreateUserArticleLikePort
import com.backend.sodam.domain.articles.service.port.DeleteUserArticleLikePort
import com.backend.sodam.domain.articles.service.port.FetchUserArticleLikePort
import com.backend.sodam.domain.articles.service.port.UpdateUserArticleLikePort
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class SocialUsersArticleLikeRepository(
    private val articleJpaRepository: ArticleJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val usersArticleLikeJpaRepository: UsersArticleLikeJpaRepository
) : CreateUserArticleLikePort, FetchUserArticleLikePort, UpdateUserArticleLikePort, DeleteUserArticleLikePort {

    override fun isTarget(userType: UserType): Boolean =
        UserType.SOCIAL == userType

    @Transactional(readOnly = true)
    override fun existsArticleLike(articleId: Long, userId: String): Boolean {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        return usersArticleLikeJpaRepository.existsByArticleAndSocialUser(socialUser = socialUserEntity, article = articleEntity)
    }

    @Transactional
    override fun deleteLike(articleId: Long, userId: String) {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val socialUserLikeArticleEntity = usersArticleLikeJpaRepository.findByArticleAndSocialUser(article = articleEntity, socialUser = socialUserEntity).get()
        usersArticleLikeJpaRepository.delete(socialUserLikeArticleEntity)
    }

    @Transactional
    override fun createLike(userId: String, articleId: Long) {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        val articleEntity = articleJpaRepository.findByArticleId(articleId).get()
        val socialUserLikeArticleEntity = UsersLikeArticleEntity(article = articleEntity, socialUser = socialUserEntity)
        usersArticleLikeJpaRepository.save(socialUserLikeArticleEntity)
    }
}
