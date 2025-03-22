package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.users.entity.SocialUsersEntity
import java.util.*

interface SocialUserCustomRepository {
    fun findByProviderId(providerId: String): Optional<SocialUsersEntity>
}
