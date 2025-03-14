package com.backend.sodam.domain.users.controller.request

import com.backend.sodam.domain.users.service.command.UserSignupCommand
import com.backend.sodam.global.annottion.PasswordEncryption
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignupRequest(
    @field:Email(message = "유효한 이메일 형식이 아닙니다.")
    @field:NotBlank(message = "이메일을 입력해주세요.")
    val email: String,

    @field:NotBlank(message = "이름을 입력해주세요.")
    @field:Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
    val name: String,

    @PasswordEncryption
    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    @field:Size(min = 8, max = 30, message = "비밀번호는 8자 이상 30자 이하로 입력해주세요.")
    var password: String, // AOP 로 인코딩 처리해야하므로 var 로 선언

//    @field:NotBlank(message = "프로필 이미지를 입력해주세요.") // 추후 파일 업로드 방식 변경 예정
//    val profileImage: String,

    @field:Size(max = 200, message = "자기소개는 최대 200자까지 입력 가능합니다.")
    val introduce: String
)

fun SignupRequest.toCommand() = UserSignupCommand(
    email = email,
    name = name,
    password = password,
    profileImage = "추후에 개발할 예정",
    introduce = introduce
)
