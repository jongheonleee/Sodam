package com.backend.sodam.domain.secrets.repository

import com.backend.sodam.domain.secrets.model.SodamDetailSecret
import com.backend.sodam.domain.secrets.model.SodamSecret
import com.backend.sodam.domain.secrets.service.command.SecretSearchCommand
import com.backend.sodam.domain.secrets.service.port.CreateSecretPort
import com.backend.sodam.domain.secrets.service.port.DeleteSecretPort
import com.backend.sodam.domain.secrets.service.port.FetchSecretPort
import com.backend.sodam.domain.secrets.service.port.UpdateSecretPort
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class SecretRepository(
    private val secretJpaRepository: SecretJpaRepository
) : CreateSecretPort, FetchSecretPort, UpdateSecretPort, DeleteSecretPort {

    @Transactional(readOnly = true)
    override fun isExistsSecret(secretId: Long): Boolean =
        secretJpaRepository.existsBySecretId(secretId)

    @Transactional(readOnly = true)
    override fun findByPageBy(pageable: Pageable, secretSearchCommand: SecretSearchCommand): Page<SodamSecret> {
        return secretJpaRepository.findByPageBy(
            pageable = pageable,
            secretSearchCommand = secretSearchCommand
        )
    }

    @Transactional(readOnly = true)
    override fun findDetailBySecretId(secretId: Long): SodamDetailSecret {
        return secretJpaRepository.findDetailBySecretId(secretId)
    }

    @Transactional
    override fun increaseViewCnt(secretId: Long) {
        val secretEntity = secretJpaRepository.findBySecretId(secretId).get()
        secretEntity.increaseViewCnt()
    }
}
