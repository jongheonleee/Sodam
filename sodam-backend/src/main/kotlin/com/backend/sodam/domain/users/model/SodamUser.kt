package com.backend.sodam.domain.users.model

import com.backend.sodam.domain.users.controller.response.UserSignupResponse

// oauth로 등록된 회원의 경우 username, provioderId만 필요하기 때문에
// @JvmOverloads를 활용한 기본값 제공을 활용함
data class SodamUser( // 추후에 SodamUser, SodamSocialUser 구분해서 정의하는게 좋음
    val userId: String = "",
    val username: String = "",
    val encryptedPassword: String = "",
    val email: String = "",
    val provider: String = "",
    val providerId: String = "",
    val role: String = "",
    val introduce: String = "",
    val profileImageUrl: String = "",
    val userType: UserType? = null
) {
    fun toSignupResponse(): UserSignupResponse {
        return UserSignupResponse(
            username = this.username,
            encryptedPassword = this.encryptedPassword,
            email = this.email,
            introduce = this.introduce
        )
    }
}
