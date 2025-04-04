package com.backend.sodam.domain.positions.service

import com.backend.sodam.domain.positions.controller.response.PositionsResponse
import com.backend.sodam.domain.positions.service.port.FetchPositionPort
import com.backend.sodam.domain.positions.service.usecase.DeletePositionUseCase
import com.backend.sodam.domain.positions.service.usecase.FetchPositionUseCase
import com.backend.sodam.domain.positions.service.usecase.RegisterPositionUseCase
import com.backend.sodam.domain.positions.service.usecase.UpdatePositionUseCase
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class PositionService(
    private val fetchPositionPort: FetchPositionPort
) : FetchPositionUseCase, RegisterPositionUseCase, UpdatePositionUseCase, DeletePositionUseCase {

    override fun fetchValidPositions(): PositionsResponse {
        val fetchedValidSodamPositions = fetchPositionPort.fetchValidPositions()
        return PositionsResponse(fetchedValidSodamPositions.map { it.toResponse() })
    }
}
