package com.backend.sodam.domain.tokens.controller.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
