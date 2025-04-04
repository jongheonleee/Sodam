package com.backend.sodam.domain.secrets.repository

import com.backend.sodam.domain.secrets.entity.ViewSecretsEntity
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
@RequiredArgsConstructor
class SecretViewRepository(
    private val secretJpaRepository: SecretJpaRepository,
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val secretViewJpaRepository: SecretViewJpaRepository
) {

    @Transactional(readOnly = true)
    fun countViewToday(userId: String): Long {
        return secretViewJpaRepository.countViewToday(userId)
    }

    @Transactional
    fun create(userId: String, secretId: Long) { // 추후에 user 쪽 개선
        if (socialUserJpaRepository.existsBySocialUserId(userId)) {
            val bySocialUserId = socialUserJpaRepository.findBySocialUserId(userId).get()
            val bySecretId = secretJpaRepository.findById(secretId).get()

            val viewSecretsEntity = ViewSecretsEntity(
                userViewId = UUID.randomUUID().toString(),
                socialUser = bySocialUserId,
                secret = bySecretId
            )

            secretViewJpaRepository.save(viewSecretsEntity)
        } else if (socialUserJpaRepository.existsByProviderId(userId)) {
            val bySocialUserId = socialUserJpaRepository.findBySocialUserId(userId).get()
            val bySecretId = secretJpaRepository.findById(secretId).get()

            val viewSecretsEntity = ViewSecretsEntity(
                userViewId = UUID.randomUUID().toString(),
                socialUser = bySocialUserId,
                secret = bySecretId
            )

            secretViewJpaRepository.save(viewSecretsEntity)
        } else if (normalUserJpaRepository.existsByUserId(userId)) {
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
}
