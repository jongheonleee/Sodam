package com.backend.sodam.domain.categories.exception

import com.backend.sodam.global.commons.ErrorCode

sealed class CategoryException(val errorCode: ErrorCode) : RuntimeException() {
    class CategoryNotFoundException : CategoryException(ErrorCode.CATEGORY_NOT_FOUND)
}