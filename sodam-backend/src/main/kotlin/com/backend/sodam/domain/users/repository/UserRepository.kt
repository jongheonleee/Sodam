package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.subscriptions.model.UserSubscription
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionRepository
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.command.*
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class UserRepository(
    private val userJpaRepository: UserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val userSubscriptionRepository: UserSubscriptionRepository,
) {

    @Transactional(readOnly = true)
    fun findByUserEmail(email: String): Optional<SodamUser> {
        val foundUserEntityOptionalByUserEmail = userJpaRepository.findByUserEmail(email)
        if (foundUserEntityOptionalByUserEmail.isEmpty) {
            return Optional.empty()
        }

        val userEntity = foundUserEntityOptionalByUserEmail.get()
        val foundUserSubscriptionOptionalByUserEmail = userSubscriptionRepository.findByUserId(email)


        return Optional.of(
            SodamUser(
                userId = userEntity.userId,
                username = userEntity.userName,
                encryptedPassword = userEntity.password,
                email = userEntity.userEmail,
                introduce = userEntity.userIntroduce,
                profileImageUrl = userEntity.userImage,
                role = if (foundUserSubscriptionOptionalByUserEmail.isPresent)
                    foundUserSubscriptionOptionalByUserEmail.get().subscriptionType.toRole()
                else UserSubscription.newSubscription(userEntity.userId).subscriptionType.toRole(),
                userType = UserType.NORMAL,
            )
        )
    }

    @Transactional
    fun create(userSignupCommand: UserSignupCommand): SodamUser {
        val signupRequestUserEntity = userSignupCommand.toEntity()
        return userJpaRepository.save(signupRequestUserEntity)
                                .toDomain()
    }

    @Transactional(readOnly = true)
    fun findByProviderId(providerId: String): Optional<SodamUser> {
        val foundSocialUsersEntityOptionalByProviderId = socialUserJpaRepository.findByProviderId(providerId)
        if (foundSocialUsersEntityOptionalByProviderId.isEmpty) {
            return Optional.empty()
        }

        val socialUserEntity = foundSocialUsersEntityOptionalByProviderId.get()
        val foundUserSubscriptionOptionalByProviderId = userSubscriptionRepository.findByUserId(providerId)

        return Optional.of(
            SodamUser(
                userId = socialUserEntity.socialUserId,
                username = socialUserEntity.userName,
                provider = socialUserEntity.provider,
                providerId = socialUserEntity.providerId,
                role = if (foundUserSubscriptionOptionalByProviderId.isPresent)
                            foundUserSubscriptionOptionalByProviderId.get().subscriptionType.toRole()
                       else UserSubscription.newSubscription(socialUserEntity.socialUserId).subscriptionType.toRole(),
                userType = UserType.SOCIAL,
            )
        )
    }


    @Transactional
    fun createSocialUser(
        name: String,
        provider: String,
        providerId: String
    ) : SodamUser {
        val socialUsersEntity = SocialUsersEntity.newEntity(
            userName = name,
            provider = provider,
            providerId = providerId,
        )
        return socialUserJpaRepository.save(socialUsersEntity)
                                      .toDomain()
    }

    @Transactional(readOnly = true)
    fun findByUserId(userId: String): Optional<SodamUser> {
        val foundUserOptionalByUserId = userJpaRepository.findByUserId(userId)

        if (foundUserOptionalByUserId.isEmpty) {
            return Optional.empty()
        }

        val userEntity = foundUserOptionalByUserId.get()
        val foundUserSubscriptionOptionalByUserId = userSubscriptionRepository.findByUserId(userEntity.userEmail)

        return Optional.of(
            SodamUser(
                userId = userEntity.userId,
                username = userEntity.userName,
                encryptedPassword = userEntity.password,
                email = userEntity.userEmail,
                introduce = userEntity.userIntroduce,
                profileImageUrl = userEntity.userImage,
                role = if (foundUserSubscriptionOptionalByUserId.isPresent)
                    foundUserSubscriptionOptionalByUserId.get().subscriptionType.toRole()
                else UserSubscription.newSubscription(userEntity.userId).subscriptionType.toRole(),
                userType = UserType.NORMAL,
            )
        )
    }




}
