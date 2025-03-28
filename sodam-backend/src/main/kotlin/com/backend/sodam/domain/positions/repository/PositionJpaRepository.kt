package com.backend.sodam.domain.positions.repository

import com.backend.sodam.domain.positions.entity.PositionsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PositionJpaRepository: JpaRepository<PositionsEntity, String>, PositionsCustomRepository