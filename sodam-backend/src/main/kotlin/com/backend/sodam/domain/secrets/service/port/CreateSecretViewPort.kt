package com.backend.sodam.domain.secrets.service.port

import com.backend.sodam.domain.users.model.UserType

interface CreateSecretViewPort {
    fun isTarget(userType: UserType): Boolean
    fun create(userId: String, secretId: Long)
}
