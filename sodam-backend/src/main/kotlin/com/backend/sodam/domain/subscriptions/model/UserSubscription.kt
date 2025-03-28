package com.backend.sodam.domain.subscriptions.model

import com.backend.sodam.domain.subscriptions.entity.SubscriptionsEntity
import com.backend.sodam.domain.subscriptions.entity.UsersSubscriptionsEntity
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import java.time.LocalDateTime
import java.util.*

// 추후에 class 로 바꾸기 
data class UserSubscription(
    val userId: String,
    var subscriptionType: SubscriptionsType,
    var startAt: LocalDateTime,
    var endAt: LocalDateTime,
    var validYn: Boolean
) {
    fun off() {
        validYn = false
    }

    fun renew() {
        startAt = LocalDateTime.now()
        endAt = getEndAt(startAt)
        validYn = true
    }

    fun change(type: SubscriptionsType) {
        subscriptionType = type
    }

    fun ableToRenew(): Boolean {
        return LocalDateTime.now().isAfter(endAt)
    }

    fun ableToChange(): Boolean {
        val now = LocalDateTime.now()
        return now.isBefore(endAt) && now.isAfter(startAt) && validYn
    }

    fun toEntity(user: UsersEntity, subscription: SubscriptionsEntity): UsersSubscriptionsEntity {
        return UsersSubscriptionsEntity(
            userSubscriptionId = UUID.randomUUID().toString(),
            user = user,
            subscription = subscription,
            startAt = this.startAt,
            endAt = this.endAt,
            validYN = if (this.validYn) 0 else 1,
            subscriptionName = this.subscriptionType
        )
    }

    fun toEntity(socialUser: SocialUsersEntity, subscription: SubscriptionsEntity): UsersSubscriptionsEntity {
        return UsersSubscriptionsEntity(
            userSubscriptionId = UUID.randomUUID().toString(),
            socialUser = socialUser,
            subscription = subscription,
            startAt = this.startAt,
            endAt = this.endAt,
            validYN = if (this.validYn) 0 else 1,
            subscriptionName = this.subscriptionType
        )
    }

    companion object {
        fun newSubscription(userId: String): UserSubscription {
            val now = LocalDateTime.now()
            return UserSubscription(
                userId = userId,
                subscriptionType = SubscriptionsType.FREE,
                startAt = now,
                endAt = getEndAt(now),
                validYn = true
            )
        }

        private fun getEndAt(startAt: LocalDateTime): LocalDateTime {
            return startAt.plusDays(30)
        }
    }
}

