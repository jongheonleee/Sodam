package com.backend.sodam.domain.subscriptions.service.port

import com.backend.sodam.domain.subscriptions.model.SubscriptionsType
import com.backend.sodam.domain.subscriptions.model.UserSubscription
import com.backend.sodam.domain.users.model.UserType

interface CreateUserSubscriptionPort {
    fun isTarget(userType: UserType): Boolean
    fun createSubscription(userId: String, subscriptionType: SubscriptionsType): UserSubscription
}
