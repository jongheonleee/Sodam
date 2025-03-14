package com.backend.sodam.domain.users.service.command

data class SocialUserSignupCommand(
    val username: String,
    val provider: String,
    val providerId: String
)
