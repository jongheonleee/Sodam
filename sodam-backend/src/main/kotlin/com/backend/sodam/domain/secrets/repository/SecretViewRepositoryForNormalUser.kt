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
class SecretViewRepositoryForNormalUser(
    private val secretJpaRepository: SecretJpaRepository,
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val secretViewJpaRepository: SecretViewJpaRepository
): AbstractSecretViewRepository(secretViewJpaRepository) {

    override fun isTarget(userType: UserType): Boolean =
        userType == UserType.NORMAL

    @Transactional
    override fun create(userId: String, secretId: Long) { // 추후에 user 쪽 개선
        val byUserId = normalUserJpaRepository.findByUserId(userId).get()
        val bySecretId = secretJpaRepository.findById(secretId).get()

        val viewSecretsEntity = ViewSecretsEntity(
            userViewId = UUID.randomUUID().toString(),
            user = byUserId,
            secret = bySecretId
        )

        secretViewJpaRepository.save(viewSecretsEntity)
    }
}
