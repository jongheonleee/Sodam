package com.backend.sodam.domain.users.service.usescase

import com.backend.sodam.domain.users.controller.response.UserUpdateResponse
import com.backend.sodam.domain.users.service.command.UserUpdateCommand

interface UpdateUserUseCase {
    fun updateUserInfo(userId: String, userUpdateCommand: UserUpdateCommand): UserUpdateResponse
}