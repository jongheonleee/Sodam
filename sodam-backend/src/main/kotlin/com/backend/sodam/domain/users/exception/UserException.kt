package com.backend.sodam.domain.users.exception

import com.backend.sodam.global.commons.ErrorCode

sealed class UserException(val errorCode: ErrorCode) : RuntimeException() {
    class UserAlreadyExistsException : UserException(ErrorCode.USER_ALREADY_EXISTS)
    class UserNotFoundException : UserException(ErrorCode.USER_NOT_FOUND)
}