package com.backend.sodam.domain.positions.exception

import com.backend.sodam.global.commons.ErrorCode

sealed class PositionException(val errorCode: ErrorCode): RuntimeException() {
    class PositionNotFoundException: PositionException(ErrorCode.POSITION_NOT_FOUND)
}