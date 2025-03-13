package com.backend.sodam.domain.subscriptions.repository

import com.backend.sodam.domain.subscriptions.entity.UsersSubscriptionsEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserSubscriptionJpaRepository : JpaRepository<UsersSubscriptionsEntity, String> {
    fun findByUser(user: UsersEntity): Optional<UsersSubscriptionsEntity>
}
