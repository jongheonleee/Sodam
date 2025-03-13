package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.users.entity.UsersEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserJpaRepository : JpaRepository<UsersEntity, String> {
    fun findByUserEmail(email: String): Optional<UsersEntity>
    fun findByUserId(userId: String): Optional<UsersEntity>
}
