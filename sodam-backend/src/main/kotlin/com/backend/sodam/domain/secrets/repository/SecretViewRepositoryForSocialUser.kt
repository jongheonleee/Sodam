package com.backend.sodam.domain.secrets.repository

import com.backend.sodam.domain.secrets.entity.ViewSecretsEntity
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
@RequiredArgsConstructor
class SecretViewRepositoryForSocialUser(
    private val secretJpaRepository: SecretJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val secretViewJpaRepository: SecretViewJpaRepository
) : AbstractSecretViewRepository(secretViewJpaRepository) {

    override fun isTarget(userType: UserType): Boolean =
        userType == UserType.SOCIAL

    @Transactional
    override fun create(userId: String, secretId: Long) { // 추후에 user 쪽 개선
        val bySocialUserId = socialUserJpaRepository.findBySocialUserId(userId).get()
        val bySecretId = secretJpaRepository.findById(secretId).get()

        val viewSecretsEntity = ViewSecretsEntity(
            userViewId = UUID.randomUUID().toString(),
            socialUser = bySocialUserId,
            secret = bySecretId
        )

        secretViewJpaRepository.save(viewSecretsEntity)
    }
}
