package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.entity.UsersLikeArticleEntity
import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.exception.UsersArticleLikeException
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.repository.UserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class UsersArticleLikeRepository(
    private val userJpaRepository: UserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val articleJpaRepository: ArticleJpaRepository,
    private val usersArticleLikeJpaRepository: UsersArticleLikeJpaRepository
) {

    @Transactional(readOnly = true)
    fun existsArticleLikeForUser(articleId: Long, userId: String): Boolean {
        val foundUserEntityOptional = userJpaRepository.findByUserId(userId)
        if (foundUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundArticleEntityOptional = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleEntityOptional.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        return usersArticleLikeJpaRepository.existsByArticleAndUser(
            users = foundUserEntityOptional.get(),
            article = foundArticleEntityOptional.get()
        )
    }

    @Transactional(readOnly = true)
    fun existsArticleLikeForSocialUser(articleId: Long, socialUserId: String): Boolean {
        val foundSocialUserEntityOptional = socialUserJpaRepository.findBySocialUserId(socialUserId)
        if (foundSocialUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundArticleEntityOptional = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleEntityOptional.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        return usersArticleLikeJpaRepository.existsByArticleAndSocialUser(
            socialUser = foundSocialUserEntityOptional.get(),
            article = foundArticleEntityOptional.get()
        )
    }

    @Transactional
    fun deleteForSocialUser(articleId: Long, userId: String) {
        val foundSocialUserEntityOptional = socialUserJpaRepository.findBySocialUserId(userId)
        if (foundSocialUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundArticleEntityOptional = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleEntityOptional.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        val foundUsersArticleLikeEntityOptional = usersArticleLikeJpaRepository.findByArticleAndSocialUser(
            article = foundArticleEntityOptional.get(),
            socialUser = foundSocialUserEntityOptional.get()
        )

        if (foundUsersArticleLikeEntityOptional.isEmpty) {
            throw UsersArticleLikeException.UsersArticleLikeNotFoundException()
        }

        val foundUserArticleLikeEntity = foundUsersArticleLikeEntityOptional.get()
        usersArticleLikeJpaRepository.delete(foundUserArticleLikeEntity)
    }

    @Transactional
    fun deleteForUser(articleId: Long, userId: String) {
        val foundUserEntityOptional = userJpaRepository.findByUserId(userId)
        if (foundUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundArticleEntityOptional = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleEntityOptional.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        val foundUsersArticleLikeEntityOptional = usersArticleLikeJpaRepository.findByArticleAndUser(
            article = foundArticleEntityOptional.get(),
            users = foundUserEntityOptional.get()
        )

        if (foundUsersArticleLikeEntityOptional.isEmpty) {
            throw UsersArticleLikeException.UsersArticleLikeNotFoundException()
        }

        val foundUserArticleLikeEntity = foundUsersArticleLikeEntityOptional.get()
        usersArticleLikeJpaRepository.delete(foundUserArticleLikeEntity)
    }

    @Transactional
    fun createForUser(articleId: Long, userId: String) {
        val foundUserEntityOptional = userJpaRepository.findByUserId(userId)
        if (foundUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundArticleEntityOptional = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleEntityOptional.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        val userArticleLikeRequestEntity = UsersLikeArticleEntity(
            article = foundArticleEntityOptional.get(),
            user = foundUserEntityOptional.get()
        )
        usersArticleLikeJpaRepository.save(userArticleLikeRequestEntity)
    }

    @Transactional
    fun createForSocialUser(articleId: Long, userId: String) {
        val foundSocialUserEntityOptional = socialUserJpaRepository.findBySocialUserId(userId)
        if (foundSocialUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundArticleEntityOptional = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleEntityOptional.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        val userArticleLikeRequestEntity = UsersLikeArticleEntity(
            article = foundArticleEntityOptional.get(),
            socialUser = foundSocialUserEntityOptional.get()
        )

        usersArticleLikeJpaRepository.save(userArticleLikeRequestEntity)
    }
}
