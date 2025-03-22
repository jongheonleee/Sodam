package com.backend.sodam.domain.tokens.exception

import com.backend.sodam.global.commons.ErrorCode

sealed class TokenException(val errorCode: ErrorCode) : RuntimeException() {
    class InvalidTokenException : TokenException(ErrorCode.INVALID_TOKEN)
    class UserTokenNotFoundException : TokenException(ErrorCode.USER_TOKEN_NOT_FOUND)
}
