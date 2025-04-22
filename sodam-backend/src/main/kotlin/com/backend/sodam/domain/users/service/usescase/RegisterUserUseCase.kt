package com.backend.sodam.domain.users.service.usescase

import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import com.backend.sodam.domain.users.service.command.UserSignupCommand
import com.backend.sodam.domain.users.service.response.UserSignupResponse

interface RegisterUserUseCase {
    fun registerNormalUser(userSignupCommand: UserSignupCommand): UserSignupResponse
    fun registerSocialUser(socialUserSignupCommand: SocialUserSignupCommand): UserSignupResponse
}
