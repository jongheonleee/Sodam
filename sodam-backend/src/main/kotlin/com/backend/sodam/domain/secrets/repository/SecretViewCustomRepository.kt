package com.backend.sodam.domain.secrets.repository

interface SecretViewCustomRepository {
    fun countViewToday(userId: String): Long
}
