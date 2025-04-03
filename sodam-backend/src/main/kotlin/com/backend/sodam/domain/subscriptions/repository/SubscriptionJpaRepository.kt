package com.backend.sodam.domain.subscriptions.repository

import com.backend.sodam.domain.subscriptions.entity.SubscriptionsEntity
import com.backend.sodam.domain.subscriptions.model.SubscriptionsType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SubscriptionJpaRepository : JpaRepository<SubscriptionsEntity, String> {
    fun existsBySubscriptionName(subscriptionName: String): Boolean
    fun findBySubscriptionName(subscriptionName: String): Optional<SubscriptionsEntity>
}
