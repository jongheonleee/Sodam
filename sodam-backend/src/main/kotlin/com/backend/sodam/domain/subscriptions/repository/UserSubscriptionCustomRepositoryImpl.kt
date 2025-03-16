package com.backend.sodam.domain.subscriptions.repository

import com.backend.sodam.domain.subscriptions.entity.QUsersSubscriptionsEntity
import com.backend.sodam.domain.subscriptions.entity.QUsersSubscriptionsEntity.*
import com.backend.sodam.domain.subscriptions.entity.UsersSubscriptionsEntity
import com.backend.sodam.domain.subscriptions.model.UserSubscription
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@RequiredArgsConstructor
class UserSubscriptionCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : UserSubscriptionCustomRepository {

    override fun findByUserId(userId: String): Optional<UserSubscription> {
        return jpaQueryFactory.selectFrom(usersSubscriptionsEntity)
            .fetch()
            .stream()
            .findFirst()
            .map {
                it.toDomain()
            }
    }
}