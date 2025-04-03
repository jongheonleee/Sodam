package com.backend.sodam.domain.positions.controller

import com.backend.sodam.domain.positions.controller.response.PositionsResponse
import com.backend.sodam.domain.positions.service.usecase.FetchPositionUseCase
import com.backend.sodam.global.commons.SodamApiResponse
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class PositionController(
    private val fetchPositionUseCase: FetchPositionUseCase,
) {

    @GetMapping("/api/v1/positions")
    fun fetchValidPositions(): SodamApiResponse<PositionsResponse> {
        return SodamApiResponse.ok(fetchPositionUseCase.fetchValidPositions())
    }
}
