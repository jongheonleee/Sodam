package com.backend.sodam.domain.secrets.exception

import com.backend.sodam.global.commons.ErrorCode

sealed class SecretException(val errorCode: ErrorCode) : RuntimeException() {
    class SecretNotFoundException() : SecretException(ErrorCode.SECRET_NOT_FOUND)
}
