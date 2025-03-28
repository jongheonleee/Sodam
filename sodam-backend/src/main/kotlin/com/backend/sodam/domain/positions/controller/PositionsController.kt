package com.backend.sodam.domain.positions.controller

import com.backend.sodam.domain.positions.service.PositionService
import com.backend.sodam.domain.positions.service.response.PositionsResponse
import com.backend.sodam.global.commons.SodamApiResponse
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class PositionsController (
    private val positionService: PositionService,
){

    @GetMapping("/api/v1/positions")
    fun getPositions(): SodamApiResponse<PositionsResponse> {
        return SodamApiResponse.ok(
            positionService.getValidPositions()
        )
    }
}