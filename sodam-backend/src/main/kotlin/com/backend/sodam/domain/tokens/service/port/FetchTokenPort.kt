package com.backend.sodam.domain.tokens.service.port

import com.backend.sodam.domain.tokens.service.response.TokenResponse
import com.backend.sodam.domain.users.model.UserType
import java.util.*

interface FetchTokenPort {
    fun isTarget(userType: UserType): Boolean
    fun findTokenByUserId(userId: String): Optional<TokenResponse>
}
