package com.backend.sodam.domain.subscriptions.service.port

interface FetchSubscriptionPort {
    fun isExistsBySubscriptionName(subscriptionName: String): Boolean
}