package com.backend.sodam.domain.secrets.service

import org.springframework.stereotype.Component

@Component
class SilverSecretViewValidator: SecretViewValidator {
    override fun isTarget(role: String): Boolean {
        return "[ROLE_SILVER]" == role
    }

    override fun isValidView(cnt: Long): Boolean {
        return cnt < 30
    }
}