package com.backend.sodam.domain.subscriptions.repository

import com.backend.sodam.domain.subscriptions.service.port.FetchSubscriptionPort
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class SubscriptionRepository(
    private val subscriptionJpaRepository: SubscriptionJpaRepository,
): FetchSubscriptionPort {

    override fun isExistsBySubscriptionName(subscriptionName: String): Boolean
        = subscriptionJpaRepository.existsBySubscriptionName(subscriptionName)
}