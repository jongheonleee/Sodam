package com.backend.sodam.domain.users.service.port

import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand

interface CreateSocialUserPort {
    fun createSocialUser(socialUserSignupCommand: SocialUserSignupCommand): SodamUser
}