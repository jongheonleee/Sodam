package com.backend.sodam.domain.secrets.repository

import com.backend.sodam.domain.secrets.entity.SecretsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SecretJpaRepository : JpaRepository<SecretsEntity, Long>, SecretCustomRepository {
}