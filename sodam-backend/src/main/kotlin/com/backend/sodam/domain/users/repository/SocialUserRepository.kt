package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.subscriptions.model.UserSubscription
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionRepository
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.UserType
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class SocialUserRepository(
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val userSubscriptionRepository: UserSubscriptionRepository
) {

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
