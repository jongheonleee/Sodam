package com.backend.sodam.domain.positions.service.port

import com.backend.sodam.domain.users.entity.UsersPositionsEntity
import com.backend.sodam.domain.users.model.UserType

interface UpdateUserPositionPort {
    fun isTarget(userType: UserType): Boolean
    fun upsertUserPosition(userId: String, positionId: String): UsersPositionsEntity // 이 부분 도메인 객체로 바꿀 예정
}
