package com.backend.sodam.domain.positions.service.usecase

import com.backend.sodam.domain.positions.controller.response.PositionsResponse

interface FetchPositionUseCase {
    fun fetchValidPositions(): PositionsResponse
}
