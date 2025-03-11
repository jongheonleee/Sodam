package com.backend.sodam.domain.users.service.dto

data class SignupResponse(
    val userId: String,
    val email: String,
    val name: String,
)
