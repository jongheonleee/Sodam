package com.backend.sodam.domain.positions.repository

import com.backend.sodam.domain.positions.model.SodamPosition
import com.backend.sodam.domain.positions.service.port.FetchPositionPort
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class PositionRepository(
    private val positionJpaRepository: PositionJpaRepository
): FetchPositionPort {

    @Transactional(readOnly = true)
    override fun fetchValidPositions(): List<SodamPosition> {
        return positionJpaRepository.fetchValidPositionsInOrder()
            .map { it.toDomain() }
    }

    @Transactional(readOnly = true)
    override fun isExistsByPositionId(positionId: String): Boolean {
        return positionJpaRepository.existsByPositionId(positionId)
    }

    @Transactional(readOnly = true)
    override fun isExistsByPositionName(positionName: String): Boolean {
        return positionJpaRepository.existsByPositionName(positionName)
    }
}
