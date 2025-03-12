package com.backend.sodam.domain.subscriptions.repository

import com.backend.sodam.domain.subscriptions.entity.SubscriptionsEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SubscriptionJpaRepository : JpaRepository<SubscriptionsEntity, String> {
    fun findBySubscriptionName(subscriptionName: String): Optional<SubscriptionsEntity>
}