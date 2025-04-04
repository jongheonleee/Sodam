package com.backend.sodam.domain.subscriptions.service.port

import com.backend.sodam.domain.subscriptions.model.UserSubscription
import com.backend.sodam.domain.users.model.UserType

interface FetchUserSubscriptionPort {
    fun isTarget(userType: UserType): Boolean
    fun findByUserId(userId: String): UserSubscription
}
