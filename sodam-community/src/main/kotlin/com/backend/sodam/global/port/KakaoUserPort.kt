package com.backend.sodam.global.port

import com.backend.sodam.domain.users.model.SodamUser

interface KakaoUserPort {
    fun findUserFromKakao(accessToken: String): SodamUser
}
