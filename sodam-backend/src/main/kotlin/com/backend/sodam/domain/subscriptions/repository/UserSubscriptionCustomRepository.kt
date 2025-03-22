package com.backend.sodam.domain.subscriptions.repository

import com.backend.sodam.domain.subscriptions.model.UserSubscription
import java.util.*

interface UserSubscriptionCustomRepository {
    fun findByUserId(userId: String): Optional<UserSubscription>
}
