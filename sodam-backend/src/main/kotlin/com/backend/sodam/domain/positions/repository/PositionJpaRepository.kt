package com.backend.sodam.domain.positions.repository

import com.backend.sodam.domain.positions.entity.PositionsEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PositionJpaRepository : JpaRepository<PositionsEntity, String>, PositionsCustomRepository{
    fun findByPositionId(positionId: String): Optional<PositionsEntity>
    fun findByPositionName(positionName: String): Optional<PositionsEntity>
}
