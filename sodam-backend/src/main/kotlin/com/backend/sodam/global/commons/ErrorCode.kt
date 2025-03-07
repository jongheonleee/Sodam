package com.backend.sodam.global.commons

// 공통 에러 코드 보관 이넘
enum class ErrorCode(
    val code: String,
    val desc: String,
) {
    DEFAULT_ERROR("SDE0000", "에러가 발생했습니다.");

    // 밑에 각 도메인에서 발생 가능한 에러 내용 정의할 예정

    override fun toString(): String {
        return "[$code] $desc"
    }

}