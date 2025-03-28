package com.backend.sodam.domain.positions.service

import com.backend.sodam.domain.positions.repository.PositionRepository
import com.backend.sodam.domain.positions.service.response.PositionsResponse
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class PositionService(
    private val positionRepository: PositionRepository,
) {

    fun fetchValidPositions(): PositionsResponse {
        val fetchedValidSodamPositions = positionRepository.fetchValidPositionsInOrder()
        return PositionsResponse(
            fetchedValidSodamPositions.map { it.toResponse() }
        )
    }
}