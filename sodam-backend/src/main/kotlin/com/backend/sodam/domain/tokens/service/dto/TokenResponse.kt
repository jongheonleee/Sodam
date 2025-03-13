package com.backend.sodam.domain.tokens.service.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
