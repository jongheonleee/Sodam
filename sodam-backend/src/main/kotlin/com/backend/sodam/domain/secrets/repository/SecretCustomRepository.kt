package com.backend.sodam.domain.secrets.repository

import com.backend.sodam.domain.secrets.model.SodamDetailSecret
import com.backend.sodam.domain.secrets.model.SodamSecret
import com.backend.sodam.domain.secrets.service.command.SecretSearchCommand
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface SecretCustomRepository {
    fun findByPageBy(pageable: Pageable, secretSearchCommand: SecretSearchCommand): Page<SodamSecret>
    fun findDetailBySecretId(secretId: Long): SodamDetailSecret
}
