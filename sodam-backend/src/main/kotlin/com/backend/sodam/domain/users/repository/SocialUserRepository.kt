package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.articles.controller.response.ArticleSummaryResponse
import com.backend.sodam.domain.subscriptions.repository.NormalUserSubscriptionRepository
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.SodamUserDetail
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import com.backend.sodam.domain.users.service.command.UserUpdateCommand
import com.backend.sodam.domain.users.service.port.CreateSocialUserPort
import com.backend.sodam.domain.users.service.port.DeleteUserPort
import com.backend.sodam.domain.users.service.port.FetchSocialUserPort
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
    private val userSubscriptionRepository: NormalUserSubscriptionRepository
) : CreateSocialUserPort, FetchSocialUserPort, UpdateUserPort, DeleteUserPort {

    override fun isTarget(userType: UserType): Boolean =
        UserType.SOCIAL == userType

    // providerId는 어떻게 처리할 것인가?
    @Transactional(readOnly = true)
    override fun isExistsByUserId(userId: String): Boolean =
        socialUserJpaRepository.existsBySocialUserId(userId)

    @Transactional(readOnly = true)
    override fun isExistsByEmail(email: String): Boolean =
        socialUserJpaRepository.existsByEmail(email)

    @Transactional(readOnly = true)
    override fun findByUserId(userId: String): Optional<SodamUser> =
        socialUserJpaRepository.findSodamUserByUserId(userId)

    @Transactional(readOnly = true)
    override fun isExistsByProviderId(providerId: String): Boolean =
        socialUserJpaRepository.existsByProviderId(providerId)

    @Transactional(readOnly = true)
    override fun findByEmail(email: String): SodamUser {
        val socialUsersEntity = socialUserJpaRepository.findByEmail(email).get()
        return socialUsersEntity.toDomain()
    }

    @Transactional(readOnly = true)
    override fun findProfileInfo(userId: String): Optional<SodamUserDetail> =
        socialUserJpaRepository.findProfileInfoForSocialUser(userId)

    @Transactional(readOnly = true)
    override fun findOwnArticlesByPageBy(pageable: Pageable, userId: String): Page<ArticleSummaryResponse> =
        socialUserJpaRepository.findSocialUserOwnArticlesByPageBy(socialUserId = userId, pageable = pageable)
            .map {
                ArticleSummaryResponse(
                    articleId = it.articleId,
                    title = it.title,
                    username = it.author,
                    summary = it.summary,
                    createdAt = it.createdAt,
                    tags = it.tags
                )
            }

    @Transactional(readOnly = true)
    override fun findOwnLikeArticles(pageable: Pageable, userId: String): Page<ArticleSummaryResponse> =
        socialUserJpaRepository.findSocialUserOwnLikeArticlesByPageBy(socialUserId = userId, pageable = pageable)
            .map {
                ArticleSummaryResponse(
                    articleId = it.articleId,
                    title = it.title,
                    username = it.author,
                    summary = it.summary,
                    createdAt = it.createdAt,
                    tags = it.tags
                )
            }

    @Transactional(readOnly = true)
    override fun findEntityByProviderId(providerId: String): Optional<SocialUsersEntity> {
        return socialUserJpaRepository.findByProviderId(providerId)
    }

    // 추후에 QueryDSL로 바꾸기
    @Transactional(readOnly = true)
    override fun findByProviderId(providerId: String): Optional<SodamUser> {
        val foundSocialUsersEntityOptionalByProviderId = socialUserJpaRepository.findByProviderId(providerId)
        if (foundSocialUsersEntityOptionalByProviderId.isEmpty) {
            return Optional.empty()
        }

        val socialUserEntity = foundSocialUsersEntityOptionalByProviderId.get()
        val userSubscription = userSubscriptionRepository.findByUserId(providerId)

        return Optional.of(
            SodamUser(
                userId = socialUserEntity.socialUserId,
                username = socialUserEntity.userName,
                provider = socialUserEntity.provider,
                providerId = socialUserEntity.providerId,
                role = userSubscription.subscriptionType.toRole(),
                userType = UserType.SOCIAL
            )
        )
    }

    @Transactional
    override fun createSocialUser(socialUserSignupCommand: SocialUserSignupCommand): SodamUser {
        val socialUsersEntity = socialUserSignupCommand.toEntity()
        return socialUserJpaRepository.save(socialUsersEntity)
            .toDomain()
    }

    @Transactional
    override fun updateUserInfo(userId: String, userUpdateCommand: UserUpdateCommand): SodamUser {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        socialUserEntity.update(userUpdateCommand)
        return socialUserJpaRepository.save(socialUserEntity)
                                      .toDomain()
    }

    @Transactional(readOnly = true)
    fun findBySocialUserId(userId: String): Optional<SodamUser> {
        val foundSocialUserEntity = socialUserJpaRepository.findBySocialUserId(userId)

        if (foundSocialUserEntity.isEmpty) {
            return Optional.empty()
        }

        val socialUserEntity = foundSocialUserEntity.get()
        val userSubscription = userSubscriptionRepository.findByUserId(socialUserEntity.providerId)

        return Optional.of(
            SodamUser(
                userId = socialUserEntity.socialUserId,
                username = socialUserEntity.userName,
                provider = socialUserEntity.provider,
                providerId = socialUserEntity.providerId,
                role = userSubscription.subscriptionType.toRole(),
                userType = UserType.SOCIAL
            )
        )
    }
}
