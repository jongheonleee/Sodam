package com.backend.sodam.domain.subscriptions.model

enum class SubscriptionsType(
    val desc: String
) {
    FREE("무료_구독권"),
    BRONZE("브론즈_구독권"),
    SILVER("실버_구독권"),
    GOLD("골드_구독권"),
    PLATINUM("플래티넘_구독권");

    fun toRole(): String {
        return "ROLE_${this.name}"
    }
}
