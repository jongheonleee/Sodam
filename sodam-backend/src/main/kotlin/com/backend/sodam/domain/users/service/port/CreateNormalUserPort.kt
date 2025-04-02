package com.backend.sodam.domain.users.service.port

import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.service.command.UserSignupCommand

interface CreateNormalUserPort {
    fun createUser(userSignupCommand: UserSignupCommand): SodamUser
}