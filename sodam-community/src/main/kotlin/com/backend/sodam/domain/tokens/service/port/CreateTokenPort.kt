package com.backend.sodam.domain.tokens.service.port

import com.backend.sodam.domain.tokens.service.response.TokenResponse
import com.backend.sodam.domain.users.model.UserType

interface CreateTokenPort {
    fun isTarget(userType: UserType): Boolean
    fun createToken(userId: String, accessToken: String, refreshToken: String): TokenResponse
}
