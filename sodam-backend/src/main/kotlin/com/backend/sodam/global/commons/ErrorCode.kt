package com.backend.sodam.global.commons

// 공통 에러 코드 보관 이넘
enum class ErrorCode(
    val code: String,
    val desc: String
) {
    DEFAULT_ERROR("SDE0000", "에러가 발생했습니다."),

    // 밑에 각 도메인에서 발생 가능한 에러 내용 정의할 예정
    // 회원
    USER_ALREADY_EXISTS("SDE0001", "이미 존재하는 회원입니다."),
    USER_NOT_FOUND("SDE0002", "존재하지 않는 회원입니다."),
    SOCIAL_USER_ALREADY_EXISTS("SDE0003", "이미 소셜 회원이 존재합니다."),

    // 구독권
    SUBSCRIPTION_NOT_FOUND("SDE1000", "존재하지 않는 구독권입니다.");

    override fun toString(): String {
        return "[$code] $desc"
    }
}
