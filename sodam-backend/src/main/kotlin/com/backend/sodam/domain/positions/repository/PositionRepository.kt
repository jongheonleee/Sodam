package com.backend.sodam.domain.positions.repository

import com.backend.sodam.domain.positions.model.SodamPosition
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class PositionRepository(
    private val positionJpaRepository: PositionJpaRepository
) {

    @Transactional(readOnly = true)
    fun fetchValidPositionsInOrder(): List<SodamPosition> {
        return positionJpaRepository.fetchValidPositionsInOrder()
                                    .map { it.toDomain() }
    }
}