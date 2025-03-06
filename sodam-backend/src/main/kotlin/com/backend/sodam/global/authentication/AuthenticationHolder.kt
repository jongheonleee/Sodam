package com.backend.sodam.global.authentication

import java.util.*

interface AuthenticationHolder {
    fun getAuthentication(): Optional<Authentication>
    fun setAuthentication(authentication: Authentication)
}
