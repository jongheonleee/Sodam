package com.backend.sodam.domain.secrets.service

interface SecretViewValidator {
    fun isTarget(role: String): Boolean
    fun isValidView(cnt: Long): Boolean
}
