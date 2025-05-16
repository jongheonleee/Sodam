package com.backend.sodam.domain.secrets.service.port

interface FetchSecretViewPort {
    fun countViewToday(userId: String): Long
}
