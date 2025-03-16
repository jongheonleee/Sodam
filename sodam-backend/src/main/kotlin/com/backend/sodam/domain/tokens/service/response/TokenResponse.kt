package com.backend.sodam.domain.tokens.service.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
