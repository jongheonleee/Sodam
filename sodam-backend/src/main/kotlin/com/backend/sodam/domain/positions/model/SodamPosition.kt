package com.backend.sodam.domain.positions.model

import com.backend.sodam.domain.positions.controller.response.PositionResponse

class SodamPosition(
    val positionId: String,
    val positionName: String,
    val positionOrd: Int,
    val isValid: Boolean
) {

    fun toResponse(): PositionResponse {
        return PositionResponse(
            positionId = this.positionId,
            positionName = this.positionName
        )
    }
}
