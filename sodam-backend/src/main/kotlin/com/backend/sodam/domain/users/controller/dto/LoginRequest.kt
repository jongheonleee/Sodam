package com.backend.sodam.domain.users.controller.dto

import com.backend.sodam.global.annottion.PasswordEncryption
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:Email(message = "유효한 이메일 형식이 아닙니다.")
    @field:NotBlank(message = "이메일을 입력해주세요.")
    val email: String,

    @PasswordEncryption
    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    @field:Size(min = 8, max = 30, message = "비밀번호는 8자 이상 30자 이하로 입력해주세요.")
    var password: String,
)
