package com.backend.sodam.domain.secrets.service.port

interface UpdateSecretPort {
    fun increaseViewCnt(secretId: Long)
}
