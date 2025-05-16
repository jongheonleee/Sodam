package com.backend.sodam.domain.positions.service.port

import com.backend.sodam.domain.users.model.UserType

interface CreateUserPositionPort {
    fun isTarget(userType: UserType): Boolean
    fun createByPositionId(userId: String, positionId: String)
    fun createByPositionName(userId: String, positionName: String)
}
