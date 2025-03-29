package com.backend.sodam.domain.users.controller.response

data class UserProfileResponse(
    val userId: String,
    val name: String,
    val email: String,
    val introduce: String,
    val profileImageUrl: String,
    val subscription: String,
    val articleTotalCnt: Long,
    val grade: String,
    val ranking: Long
)
