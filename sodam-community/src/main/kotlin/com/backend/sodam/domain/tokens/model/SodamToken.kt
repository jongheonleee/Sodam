package com.backend.sodam.domain.tokens.model

import java.time.LocalDateTime

class SodamToken(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpireAt: LocalDateTime,
    val refreshTokenExpireAt: LocalDateTime
)
