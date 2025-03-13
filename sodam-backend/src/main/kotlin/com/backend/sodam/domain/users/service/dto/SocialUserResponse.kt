package com.backend.sodam.domain.users.service.dto

data class SocialUserResponse(
    val name : String,
    val provider : String,
    val providerId : String,
)
