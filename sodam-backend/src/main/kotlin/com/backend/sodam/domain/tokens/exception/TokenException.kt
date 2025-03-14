package com.backend.sodam.domain.tokens.exception

import com.backend.sodam.global.commons.ErrorCode

sealed class TokenException(val errorCode: ErrorCode) : RuntimeException() {
    class UserIdNotFoundOnTokenException : TokenException(ErrorCode.USER_ID_NOT_FOUND_ON_TOKEN)
    class UserTokenNotFoundException : TokenException(ErrorCode.USER_TOKEN_NOT_FOUND)
}