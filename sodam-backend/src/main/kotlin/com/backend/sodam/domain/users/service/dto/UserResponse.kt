package com.backend.sodam.domain.users.service.dto

data class UserResponse(
    val userId: String,
    val email: String,
    val name: String,
    val password: String,
    val profileImage: String, // 추후에 파일 형식으로 바꾸기
    val introduce: String,
)
