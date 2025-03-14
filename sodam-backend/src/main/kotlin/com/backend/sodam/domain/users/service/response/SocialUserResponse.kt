package com.backend.sodam.domain.users.service.response

data class SocialUserResponse(
    val name : String,
    val provider : String,
    val providerId : String,
)
