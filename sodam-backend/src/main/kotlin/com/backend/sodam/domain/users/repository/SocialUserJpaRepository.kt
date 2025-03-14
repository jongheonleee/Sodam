package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.users.entity.SocialUsersEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SocialUserJpaRepository : JpaRepository<SocialUsersEntity, String>, SocialUserCustomRepository {
    fun findBySocialUserId(socialUserId: String): Optional<SocialUsersEntity>
}