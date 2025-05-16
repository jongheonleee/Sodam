package com.backend.sodam.global.port

interface KakaoTokenPort {
    fun getAccessTokenByCode(code: String): String
}
