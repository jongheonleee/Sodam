package com.backend.sodam.domain.users.service.response

data class UserUpdateResponse(
    val username: String,
    val encryptedPassword: String = "",
    val email: String = "",
    val introduce: String = ""
)
