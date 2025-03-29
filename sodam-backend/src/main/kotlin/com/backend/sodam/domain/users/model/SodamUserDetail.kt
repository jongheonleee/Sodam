package com.backend.sodam.domain.users.model

import com.backend.sodam.domain.subscriptions.model.SubscriptionsType
import com.backend.sodam.domain.users.controller.response.UserProfileResponse

class SodamUserDetail(
    val userId: String,
    val name: String,
    val email: String,
    val introduce: String,
    val profileImageUrl: String,
    val subscription: SubscriptionsType,
    val articleTotalCnt: Long,
    val grade: String,
    val ranking: Long
) {

    fun toResponse(): UserProfileResponse {
        return UserProfileResponse(
            userId = this.userId,
            name = this.name,
            email = this.email,
            introduce = this.introduce,
            profileImageUrl = this.profileImageUrl,
            subscription = this.subscription.name,
            articleTotalCnt = this.articleTotalCnt,
            grade = this.grade,
            ranking = this.ranking
        )
    }
}
