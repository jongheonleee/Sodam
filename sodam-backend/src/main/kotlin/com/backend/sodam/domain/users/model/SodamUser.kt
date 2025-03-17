package com.backend.sodam.domain.users.model

// oauth로 등록된 회원의 경우 username, provioderId만 필요하기 때문에
// @JvmOverloads를 활용한 기본값 제공을 활용함
data class SodamUser(
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
)

