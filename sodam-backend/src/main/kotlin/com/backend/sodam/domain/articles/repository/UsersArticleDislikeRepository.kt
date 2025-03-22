package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.entity.UsersDislikeArticleEntity
import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.exception.UsersArticleDislikeException
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.repository.UserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class UsersArticleDislikeRepository(
    private val userJpaRepository: UserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val articleJpaRepository: ArticleJpaRepository,
    private val usersArticleDislikeJpaRepository: UsersArticleDislikeJpaRepository
) {

    @Transactional(readOnly = true)
    fun existsArticleDislikeForSocialUser(articleId: Long, socialUserId: String): Boolean {
        val foundSocialUserEntityOptional = socialUserJpaRepository.findBySocialUserId(socialUserId)
        if (foundSocialUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundArticleEntityOptional = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleEntityOptional.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        return usersArticleDislikeJpaRepository.existsByArticleAndSocialUser(
            socialUser = foundSocialUserEntityOptional.get(),
            article = foundArticleEntityOptional.get()
        )
    }

    @Transactional(readOnly = true)
    fun existsArticleDislikeForUser(articleId: Long, userId: String): Boolean {
        val foundUserEntityOptional = userJpaRepository.findByUserId(userId)
        if (foundUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundArticleEntity = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleEntity.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        return usersArticleDislikeJpaRepository.existsByArticleAndUser(
            user = foundUserEntityOptional.get(),
            article = foundArticleEntity.get()
        )
    }

    @Transactional
    fun deleteForSocialUser(articleId: Long, socialUserId: String) {
        val foundSocialUserEntityOptional = socialUserJpaRepository.findBySocialUserId(socialUserId)
        if (foundSocialUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundArticleEntityOptional = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleEntityOptional.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        val foundUsersArticleDislikeEntityOptional = usersArticleDislikeJpaRepository.findByArticleAndSocialUser(
            socialUser = foundSocialUserEntityOptional.get(),
            article = foundArticleEntityOptional.get()
        )

        if (foundUsersArticleDislikeEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundUserArticleDislikeEntity = foundUsersArticleDislikeEntityOptional.get()
        usersArticleDislikeJpaRepository.delete(foundUserArticleDislikeEntity)
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

        val foundUsersArticleDislikeEntityOptional = usersArticleDislikeJpaRepository.findByArticleAndUser(
            user = foundUserEntityOptional.get(),
            article = foundArticleEntityOptional.get()
        )

        if (foundUsersArticleDislikeEntityOptional.isEmpty) {
            throw UsersArticleDislikeException.UsersArticleDislikeNotFoundException()
        }

        val foundUsersArticleDislikeEntity = foundUsersArticleDislikeEntityOptional.get()
        usersArticleDislikeJpaRepository.delete(foundUsersArticleDislikeEntity)
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

        val userArticleDislikeRequestEntity = UsersDislikeArticleEntity(
            article = foundArticleEntityOptional.get(),
            user = foundUserEntityOptional.get()
        )

        usersArticleDislikeJpaRepository.save(userArticleDislikeRequestEntity)
    }

    @Transactional
    fun createForSocialUser(articleId: Long, socialUserId: String) {
        val foundSocialUserEntityOptional = socialUserJpaRepository.findBySocialUserId(socialUserId)
        if (foundSocialUserEntityOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundArticleEntityOptional = articleJpaRepository.findByArticleId(articleId)
        if (foundArticleEntityOptional.isEmpty) {
            throw ArticleException.ArticleNotFoundException()
        }

        val userArticleDislikeRequestEntity = UsersDislikeArticleEntity(
            article = foundArticleEntityOptional.get(),
            socialUser = foundSocialUserEntityOptional.get()
        )

        usersArticleDislikeJpaRepository.save(userArticleDislikeRequestEntity)
    }
}
