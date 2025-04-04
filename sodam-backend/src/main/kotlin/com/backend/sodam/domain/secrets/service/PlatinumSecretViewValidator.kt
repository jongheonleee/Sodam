package com.backend.sodam.domain.secrets.service

import org.springframework.stereotype.Component

@Component
class PlatinumSecretViewValidator : SecretViewValidator {
    override fun isTarget(role: String): Boolean {
        return "[ROLE_PLATINUM]" == role
    }

    override fun isValidView(cnt: Long): Boolean {
        return cnt < 100
    }
}
