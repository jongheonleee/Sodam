package com.backend.sodam.domain.users.service.dto

import com.backend.sodam.domain.users.entity.UsersEntity
import java.util.*

data class SignupRequestDto (
    val email: String,
    val name: String,
    val password: String,
    val profileImage: String, // 추후에 파일 형식으로 바꾸기
    val introduce: String,
)

fun SignupRequestDto.toEntity() = UsersEntity(
    userId = UUID.randomUUID().toString(),
    userEmail = email,
    userName = name,
    userIntroduce = introduce,
    userImage = profileImage,
    password = password,
)