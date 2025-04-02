package com.backend.sodam.domain.users.service.command

import com.backend.sodam.domain.users.entity.SocialUsersEntity
import java.util.*

data class SocialUserSignupCommand(
    val username: String,
    val provider: String,
    val providerId: String
) {
    fun toEntity(): SocialUsersEntity {
        return SocialUsersEntity(
            socialUserId = UUID.randomUUID().toString(),
            userName = this.username,
            provider = this.provider,
            providerId = this.providerId,
        )
    }
}
