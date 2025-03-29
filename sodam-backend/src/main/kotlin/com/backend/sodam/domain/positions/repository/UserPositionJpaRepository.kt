package com.backend.sodam.domain.positions.repository

import com.backend.sodam.domain.users.entity.UsersPositionsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserPositionJpaRepository: JpaRepository<UsersPositionsEntity, String>, UserPositionCustomRepository