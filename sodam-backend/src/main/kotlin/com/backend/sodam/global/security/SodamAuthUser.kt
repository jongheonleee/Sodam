package com.backend.sodam.global.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class SodamAuthUser(
    val userId: String,
    val email: String,
    username: String,
    password: String,
    authorities: Collection<GrantedAuthority>
) : User(username, password, authorities) {
    // 추가 메서드나 필드를 여기에 추가할 수 있습니다.
}