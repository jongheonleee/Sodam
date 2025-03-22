package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.users.entity.UsersHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserHistoryJpaRepository : JpaRepository<UsersHistoryEntity, Long>
