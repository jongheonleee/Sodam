package com.backend.sodam.domain.grades.exception

import com.backend.sodam.global.commons.ErrorCode

sealed class GradeException(val errorCode: ErrorCode): RuntimeException() {
    class GradeNotFoundException: GradeException(ErrorCode.GRADE_NOT_FOUND)
}