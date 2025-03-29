package com.backend.sodam.domain.users.controller.response

import com.backend.sodam.domain.users.model.SodamUser

data class UserResponse(
    val userId: String,
    val email: String = "",
    val name: String,
    val password: String = "",
    val profileImage: String = "", // 추후에 파일 형식으로 바꾸기
    val introduce: String = "",
    val role: String = ""
) {
    companion object {
        fun toUserResponse(sodamUser: SodamUser): UserResponse {
            return UserResponse(
                userId = sodamUser.userId,
                email = sodamUser.email,
                name = sodamUser.username,
                password = sodamUser.encryptedPassword,
                profileImage = sodamUser.profileImageUrl,
                introduce = sodamUser.introduce,
                role = sodamUser.role
            )
        }
    }
}
