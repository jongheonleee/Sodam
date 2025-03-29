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
    SUBSCRIPTION_NOT_FOUND("SDE1000", "존재하지 않는 구독권입니다."),

    // 토큰
    INVALID_TOKEN("SDE2000", "권한 정보가 없는 토큰입니다.."),
    USER_TOKEN_NOT_FOUND("SDE2001", "해당 회원의 토큰을 찾을 수 없습니다."),

    // 게시글
    ARTICLE_NOT_FOUND("SDE3000", "해당 게시글을 찾을 수 없습니다."),
    ARTICLE_ACCESS_DENIAL("SDE3001", "해당 게시글의 권한이 없습니다."),
    USERS_ARTICLE_LIKE_NOT_FOUND("SDE3002", "해당 회원 좋아요 게시글 기록을 찾을 수 없습니다."),
    USERS_ARTICLE_DISLIKE_NOT_FOUND("SDE3003", "해당 회원 싫어요 게시글 기록을 찾을 수 없습니다."),

    // 댓글
    COMMENT_NOT_FOUND("SDE4000", "해당 댓글을 찾을 수 없습니다."),
    COMMENT_ACCESS_DENIAL("SDE4001", "해당 게시글의 권한이 없습니다."),
    USERS_COMMENT_LIKE_NOT_FOUND("SDE4002", "해당 회원 좋아요 댓글 기록을 찾을 수 없습니다."),
    USERS_COMMENT_DISLIKE_NOT_FOUND("SDE4003", "해당 회원 싫어요 댓글 기록을 찾을 수 없습니다."),

    // 시크릿
    SECRET_NOT_FOUND("SDE5000", "해당 시크릿을 찾을 수 없습니다."),

    // 카테고리
    CATEGORY_NOT_FOUND("SDE4001", "해당 카테고리를 찾을 수 없습니다."),

    // 등급
    GRADE_NOT_FOUND("SDE5000", "해당 등급을 찾을 수 없습니다."),

    // 포지션
    POSITION_NOT_FOUND("SDE6000", "해당 포지션을 찾을 수 없습니다.");

    override fun toString(): String {
        return "[$code] $desc"
    }
}
