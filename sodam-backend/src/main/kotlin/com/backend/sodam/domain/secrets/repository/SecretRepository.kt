package com.backend.sodam.domain.secrets.repository

import com.backend.sodam.domain.secrets.exception.SecretException
import com.backend.sodam.domain.secrets.model.SodamDetailSecret
import com.backend.sodam.domain.secrets.model.SodamSecret
import com.backend.sodam.domain.secrets.service.command.SecretSearchCommand
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class SecretRepository(
    private val secretJpaRepository: SecretJpaRepository
) {

    @Transactional(readOnly = true)
    fun findByPageBy(pageable: Pageable, secretSearchCommand: SecretSearchCommand): Page<SodamSecret> {
        return secretJpaRepository.findByPageBy(
            pageable = pageable,
            secretSearchCommand = secretSearchCommand
        )
    }

    @Transactional
    fun increaseViewCnt(secretId: Long) {
        val foundSecretEntityOptional = secretJpaRepository.findBySecretId(secretId)
        if (foundSecretEntityOptional.isEmpty) {
            throw SecretException.SecretNotFoundException()
        }

        val foundSecretEntity = foundSecretEntityOptional.get()
        foundSecretEntity.increaseViewCnt()
    }

    @Transactional(readOnly = true)
    fun findDetailBySecretId(secretId: Long): SodamDetailSecret {
        return secretJpaRepository.findDetailBySecretId(secretId)
    }
}
