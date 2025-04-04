package com.backend.sodam.domain.subscriptions.repository

import com.backend.sodam.domain.subscriptions.model.SubscriptionsType
import com.backend.sodam.domain.subscriptions.model.UserSubscription
import com.backend.sodam.domain.subscriptions.service.port.CreateUserSubscriptionPort
import com.backend.sodam.domain.subscriptions.service.port.DeleteUserSubscriptionPort
import com.backend.sodam.domain.subscriptions.service.port.FetchUserSubscriptionPort
import com.backend.sodam.domain.subscriptions.service.port.UpdateUserSubscriptionPort
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class SocialUserSubscriptionRepository(
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val subscriptionJpaRepository: SubscriptionJpaRepository,
    private val userSubscriptionJpaRepository: UserSubscriptionJpaRepository
) : CreateUserSubscriptionPort, FetchUserSubscriptionPort, UpdateUserSubscriptionPort, DeleteUserSubscriptionPort {

    override fun isTarget(userType: UserType): Boolean =
        UserType.SOCIAL == userType

    @Transactional
    override fun createSubscription(userId: String, subscriptionType: SubscriptionsType): UserSubscription {
        val userSubscription = UserSubscription.newSubscription(userId = userId, subscriptionType = subscriptionType)
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        val subscriptionEntity = subscriptionJpaRepository.findBySubscriptionName(subscriptionName = subscriptionType.name).get()
        val userSubscriptionEntity = userSubscription.toEntity(socialUser = socialUserEntity, subscription = subscriptionEntity)
        userSubscriptionJpaRepository.save(userSubscriptionEntity)
        return userSubscription
    }

    @Transactional(readOnly = true)
    override fun findByUserId(userId: String): UserSubscription =
        userSubscriptionJpaRepository.findByUserId(userId).get()
}
