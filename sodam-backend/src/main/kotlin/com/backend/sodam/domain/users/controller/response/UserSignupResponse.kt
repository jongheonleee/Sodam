package com.backend.sodam.domain.users.controller.response

data class UserSignupResponse(
    val username: String,
    val encryptedPassword: String = "",
    val email: String = "",
    val introduce: String = ""
)
