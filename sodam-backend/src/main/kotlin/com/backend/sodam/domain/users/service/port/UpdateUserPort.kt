package com.backend.sodam.domain.users.service.port

import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.command.UserUpdateCommand

interface UpdateUserPort {
    fun isTarget(userType: UserType): Boolean
    fun updateUserInfo(userId: String, userUpdateCommand: UserUpdateCommand): SodamUser
}
