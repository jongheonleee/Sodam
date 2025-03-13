package com.backend.sodam.domain.subscriptions.model

enum class SubscriptionsType(
    val desc: String
) {
    FREE("무료 구독권"),
    BRONZE("브론즈 구독권"),
    SILVER("실버 구독권"),
    GOLD("골드 구독권"),
    PLATINUM("플래티넘 구독권");

    fun toRole(): String {
        return "ROLE_${this.name}"
    }
}
