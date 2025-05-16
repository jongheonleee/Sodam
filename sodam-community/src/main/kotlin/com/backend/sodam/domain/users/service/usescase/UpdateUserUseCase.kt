package com.backend.sodam.domain.users.service.usescase

import com.backend.sodam.domain.users.service.command.UserUpdateCommand
import com.backend.sodam.domain.users.service.response.UserUpdateResponse

interface UpdateUserUseCase {
    fun updateUserInfo(userId: String, userUpdateCommand: UserUpdateCommand): UserUpdateResponse
}
