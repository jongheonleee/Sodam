package com.backend.sodam.domain.positions.service.port

import com.backend.sodam.domain.positions.model.SodamPosition

interface FetchPositionPort {
    fun isExistsByPositionId(positionId: String): Boolean
    fun isExistsByPositionName(positionName: String): Boolean
    fun fetchValidPositions(): List<SodamPosition>
}