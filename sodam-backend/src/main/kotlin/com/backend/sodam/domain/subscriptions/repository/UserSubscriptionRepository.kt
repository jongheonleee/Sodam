package com.backend.sodam.domain.subscriptions.repository

import com.backend.sodam.domain.subscriptions.model.UserSubscription
import com.backend.sodam.domain.subscriptions.model.toEntity
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.repository.UserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Repository
@RequiredArgsConstructor
class UserSubscriptionRepository(
    private val userJpaRepository: UserJpaRepository,
    private val userSubscriptionJpaRepository: UserSubscriptionJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val subscriptionJpaRepository: SubscriptionJpaRepository
) {

    // 회원 구독권 도메인을 생성한다.
    // 회원 엔티티를 조회한다.
    // 무료 구독권 엔티티를 조회한다.
    // 회원 보유 구독권 엔티티를 생성한다.
    // 생성한 엔티티를 저장하고 리스폰스로 반환한다.
    @Transactional
    fun createSubscriptionForUser(userId: String): UserSubscription {
        val userSubscription = UserSubscription.newSubscription(userId)
        println(userSubscription.subscriptionType)

        val foundUserOptionalEntity = userJpaRepository.findByUserId(userId)

        if (foundUserOptionalEntity.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundSubscriptionOptionalEntity = subscriptionJpaRepository.findBySubscriptionName(userSubscription.subscriptionType.name)

        val userSubscriptionEntity = userSubscription.toEntity(
            foundUserOptionalEntity.get(),
            foundSubscriptionOptionalEntity.get()
        )

        userSubscriptionJpaRepository.save(userSubscriptionEntity)
        return userSubscription
    }

    @Transactional
    fun createUserSubscriptionForSocialUser(socialUserId: String): UserSubscription {
        val userSubscription = UserSubscription.newSubscription(socialUserId)

        val foundSocialUserOptionalEntity = socialUserJpaRepository.findBySocialUserId(socialUserId)

        if (foundSocialUserOptionalEntity.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val foundSubscriptionOptionalEntity = subscriptionJpaRepository.findBySubscriptionName(userSubscription.subscriptionType.name)

        val userSubscriptionEntity = userSubscription.toEntity(
            foundSocialUserOptionalEntity.get(),
            foundSubscriptionOptionalEntity.get()
        )

        userSubscriptionJpaRepository.save(userSubscriptionEntity)
        return userSubscription
    }

    @Transactional(readOnly = true)
    fun findByUserId(userId: String): Optional<UserSubscription> {
        return userSubscriptionJpaRepository.findByUserId(userId)
    }
}
