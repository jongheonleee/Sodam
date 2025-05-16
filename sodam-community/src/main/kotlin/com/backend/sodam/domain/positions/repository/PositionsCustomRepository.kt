package com.backend.sodam.domain.positions.repository

import com.backend.sodam.domain.positions.entity.PositionsEntity

interface PositionsCustomRepository {
    fun fetchValidPositionsInOrder(): List<PositionsEntity>
}
