package com.backend.sodam.domain.secrets.repository

import com.backend.sodam.domain.secrets.entity.ViewSecretsEntity
import com.backend.sodam.domain.secrets.service.port.CreateSecretViewPort
import com.backend.sodam.domain.secrets.service.port.DeleteSecretViewPort
import com.backend.sodam.domain.secrets.service.port.FetchSecretViewPort
import com.backend.sodam.domain.secrets.service.port.UpdateSecretViewPort
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
@RequiredArgsConstructor
abstract class AbstractSecretViewRepository(
    private val secretViewJpaRepository: SecretViewJpaRepository
): CreateSecretViewPort, FetchSecretViewPort, UpdateSecretViewPort, DeleteSecretViewPort {

    abstract override fun isTarget(userType: UserType): Boolean
    abstract override fun create(userId: String, secretId: Long)

    @Transactional(readOnly = true)
    override fun countViewToday(userId: String): Long =
        secretViewJpaRepository.countViewToday(userId)

}
