package com.backend.sodam.domain.positions.service.port

interface FetchPositionPort {
    fun isExistsByPositionId(positionId: String): Boolean
    fun isExistsByPositionName(positionName: String): Boolean
}