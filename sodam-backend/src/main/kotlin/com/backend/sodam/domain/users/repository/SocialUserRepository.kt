package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.articles.controller.response.ArticleSummaryResponse
import com.backend.sodam.domain.subscriptions.model.UserSubscription
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionRepository
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.SodamUserDetail
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.port.CreateUserPort
import com.backend.sodam.domain.users.service.port.DeleteUserPort
import com.backend.sodam.domain.users.service.port.FetchUserPort
import com.backend.sodam.domain.users.service.port.UpdateUserPort
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
 class SocialUserRepository(
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val userSubscriptionRepository: UserSubscriptionRepository
): CreateUserPort, FetchUserPort, UpdateUserPort, DeleteUserPort {

    override fun isTarget(userType: UserType): Boolean
        = UserType.SOCIAL == userType

    // providerId는 어떻게 처리할 것인가?
    @Transactional(readOnly = true)
    override fun isExistsByUserId(userId: String): Boolean
        = socialUserJpaRepository.existsBySocialUserId(userId)

    @Transactional(readOnly = true)
    override fun isExistsByEmail(email: String): Boolean
        = socialUserJpaRepository.existsByEmail(email)

    @Transactional(readOnly = true)
    override fun findByUserId(userId: String): Optional<SodamUser>
        = socialUserJpaRepository.findSodamUserByUserId(userId)


    @Transactional(readOnly = true)
    override fun findByEmail(email: String): SodamUser {
        val socialUsersEntity = socialUserJpaRepository.findByEmail(email).get()
        return socialUsersEntity.toDomain()
    }

    @Transactional(readOnly = true)
    override fun findProfileInfo(userId: String): Optional<SodamUserDetail>
        = socialUserJpaRepository.findProfileInfoForSocialUser(userId)


    @Transactional(readOnly = true)
    override fun findOwnArticlesByPageBy(pageable: Pageable, userId: String): Page<ArticleSummaryResponse>
        = socialUserJpaRepository.findSocialUserOwnArticlesByPageBy( socialUserId = userId, pageable = pageable)
                                 .map { ArticleSummaryResponse(  articleId = it.articleId,
                                                                                title = it.title,
                                                                                username = it.author,
                                                                                summary = it.summary,
                                                                                createdAt = it.createdAt,
                                                                                tags = it.tags)
        }

    @Transactional(readOnly = true)
    override fun findOwnLikeArticles(pageable: Pageable, userId: String): Page<ArticleSummaryResponse>
        = socialUserJpaRepository.findSocialUserOwnLikeArticlesByPageBy( socialUserId = userId, pageable = pageable)
                                 .map { ArticleSummaryResponse( articleId = it.articleId,
                                                                               title = it.title,
                                                                               username = it.author,
                                                                               summary = it.summary,
                                                                               createdAt = it.createdAt,
                                                                               tags = it.tags)
        }
    @Transactional(readOnly = true)
    fun findByProviderId(providerId: String): Optional<SocialUsersEntity> {
        return socialUserJpaRepository.findByProviderId(providerId)
    }

    @Transactional(readOnly = true)
    fun findBySocialUserId(userId: String): Optional<SodamUser> {
        val foundSocialUserEntity = socialUserJpaRepository.findBySocialUserId(userId)

        if (foundSocialUserEntity.isEmpty) {
            return Optional.empty()
        }

        val socialUserEntity = foundSocialUserEntity.get()
        val foundUserSubscriptionOptionalByProviderId = userSubscriptionRepository.findByUserId(socialUserEntity.providerId)

        return Optional.of(
            SodamUser(
                userId = socialUserEntity.socialUserId,
                username = socialUserEntity.userName,
                provider = socialUserEntity.provider,
                providerId = socialUserEntity.providerId,
                role = if (foundUserSubscriptionOptionalByProviderId.isPresent) {
                    foundUserSubscriptionOptionalByProviderId.get().subscriptionType.toRole()
                } else {
                    UserSubscription.newSubscription(socialUserEntity.socialUserId).subscriptionType.toRole()
                },
                userType = UserType.SOCIAL
            )
        )
    }
}
