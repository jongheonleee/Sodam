package com.backend.sodam.domain.subscriptions.repository

import UserSubscription
import com.backend.sodam.domain.subscriptions.exception.SubscriptionException
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.UserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import toEntity

@Repository
@RequiredArgsConstructor
class UserSubscriptionRepository(
    private val userJpaRepository: UserJpaRepository,
    private val userSubscriptionJpaRepository: UserSubscriptionJpaRepository,
    private val subscriptionJpaRepository : SubscriptionJpaRepository
) {

    // 회원 구독권 도메인을 생성한다.
    // 회원 엔티티를 조회한다.
    // 무료 구독권 엔티티를 조회한다.
    // 회원 보유 구독권 엔티티를 생성한다.
    // 생성한 엔티티를 저장하고 리스폰스로 반환한다.
    @Transactional
    fun create(userId : String) : UserSubscription {
        val userSubscription = UserSubscription.newSubscription(userId)
        val foundUserEntity = userJpaRepository.findByUserId(userId)
                                               .orElseThrow { UserException.UserNotFoundException() }
        val foundSubscriptionEntity = subscriptionJpaRepository.findBySubscriptionName(userSubscription.subscriptionType.desc)
                                                               .orElseThrow { SubscriptionException.SubscriptionNotFoundException() }
        val userSubscriptionEntity = userSubscription.toEntity( foundUserEntity,
                                                                foundSubscriptionEntity,)
        userSubscriptionJpaRepository.save(userSubscriptionEntity)
        return userSubscription
    }
}