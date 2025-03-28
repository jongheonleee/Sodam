package com.backend.sodam.domain.positions.service

import com.backend.sodam.domain.positions.repository.PositionRepository
import com.backend.sodam.domain.positions.service.response.PositionsResponse
import com.backend.sodam.global.commons.SodamApiResponse
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class PositionService(
    private val positionRepository: PositionRepository,
) {

    fun getValidPositions(): PositionsResponse {
        val fetchValidSodamPositions = positionRepository.fetchValidPositionsInOrder()
        return PositionsResponse(
            fetchValidSodamPositions.map { it.toResponse() }
        )
    }
}