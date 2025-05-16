package com.backend.sodam.domain.secrets.repository

import com.backend.sodam.domain.secrets.entity.ViewSecretsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SecretViewJpaRepository : JpaRepository<ViewSecretsEntity, String>, SecretViewCustomRepository
