package com.backend.sodam.domain.secrets.service

import org.springframework.stereotype.Component

@Component
class GoldSecretViewValidator: SecretViewValidator {
    override fun isTarget(role: String): Boolean {
        return "[ROLE_GOLD]" == role
    }

    override fun isValidView(cnt: Long): Boolean {
        return cnt < 50
    }
}