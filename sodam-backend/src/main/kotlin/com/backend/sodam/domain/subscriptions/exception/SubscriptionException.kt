package com.backend.sodam.domain.subscriptions.exception
import com.backend.sodam.global.commons.ErrorCode

sealed class SubscriptionException(val errorCode: ErrorCode) : RuntimeException() {
     class SubscriptionNotFoundException : SubscriptionException(ErrorCode.SUBSCRIPTION_NOT_FOUND)
}