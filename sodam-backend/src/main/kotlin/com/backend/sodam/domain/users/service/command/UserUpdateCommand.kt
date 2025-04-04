package com.backend.sodam.domain.users.service.command

data class UserUpdateCommand(
    val email: String,
    val name: String,
    val encryptedPassword: String,
    val positionId: String,
    val introduce: String
)
