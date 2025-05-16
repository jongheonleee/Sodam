package com.backend.sodam.global.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class SodamAuthUser(
    val userId: String,
    val email: String,
    username: String,
    password: String,
    authorities: Collection<GrantedAuthority>
) : User(username, password, authorities)
