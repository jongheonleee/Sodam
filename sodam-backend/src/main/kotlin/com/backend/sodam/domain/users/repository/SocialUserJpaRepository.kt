package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.model.SodamUser
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SocialUserJpaRepository : JpaRepository<SocialUsersEntity, String>, SocialUserCustomRepository {
    fun existsByEmail(email: String): Boolean
    fun existsByProviderId(providerId: String): Boolean
    fun existsBySocialUserId(socialId: String): Boolean
    fun findByEmail(email: String): Optional<SocialUsersEntity>
    fun findByProviderId(userId: String): Optional<SocialUsersEntity>
    fun findBySocialUserId(socialUserId: String): Optional<SocialUsersEntity>
}
