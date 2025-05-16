package com.backend.sodam.domain.secrets.service

import org.springframework.stereotype.Component

@Component
class BronzeSecretViewValidator : SecretViewValidator {
    override fun isTarget(role: String): Boolean {
        return "[ROLE_BRONZE]" == role
    }

    override fun isValidView(cnt: Long): Boolean {
        return cnt < 15
    }
}
