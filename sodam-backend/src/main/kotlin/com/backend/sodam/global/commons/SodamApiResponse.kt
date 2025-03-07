package com.backend.sodam.global.commons

// 공통 API 반환 클래스
data class SodamApiResponse<T>(
    val success: Boolean,
    val code: String,
    val message: String?,
    val data: T?
) {
    //  클래스 내부에서 정의되는 싱글턴 객체로, 자바의 static 멤버와 유사한 역할을 함
    //  => 해당 부분을 'companion object' 로 적용한 이유는 팩토리 메서드로서 오브젝트를 생성하는 역할을 담당하기 때문
    companion object {
        private const val CODE_SUCCESS = "success"

        fun <T> ok(data: T): SodamApiResponse<T> {
            return SodamApiResponse(true, CODE_SUCCESS, null, data)
        }

        fun fail(errorCode: ErrorCode, message: String): SodamApiResponse<Nothing> {
            return SodamApiResponse(false, errorCode.code, message, null)
        }
    }
}
