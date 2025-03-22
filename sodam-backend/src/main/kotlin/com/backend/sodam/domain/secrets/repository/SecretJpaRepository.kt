package com.backend.sodam.domain.secrets.repository

import com.backend.sodam.domain.secrets.entity.SecretsEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface SecretJpaRepository : JpaRepository<SecretsEntity, Long>, SecretCustomRepository {
    fun findBySecretId(secretId: Long): Optional<SecretsEntity>
}
