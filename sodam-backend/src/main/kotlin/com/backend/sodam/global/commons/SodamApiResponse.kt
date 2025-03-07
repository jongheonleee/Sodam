package com.backend.sodam.global.commons

// 공통 API 반환 클래스
data class SodamApiResponse<T>(
    val success: Boolean,
    val code: String,
    val message: String?,
    val data: T?
) {
    companion object {
        const val CODE_SUCCESS = "success"

        fun <T> ok(data: T) : SodamApiResponse<T> {
            return SodamApiResponse(true, CODE_SUCCESS, null, data)
        }

        fun fail(errorCode: ErrorCode, message: String) : SodamApiResponse<Nothing> {
            return SodamApiResponse(false, errorCode.code, message, null)
        }
    }
}
