package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.service.command.*
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class UserRepository(
    private val userJpaRepository: UserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
) {

    @Transactional(readOnly = true)
    fun findByUserEmail(email: String): Optional<SodamUser> {
        return userJpaRepository.findByUserEmail(email)
                                .map { it.toDomain() }
    }

    @Transactional
    fun create(userSignupCommand: UserSignupCommand): SodamUser {
        // 회원가입 진행
        val signupRequestUserEntity = userSignupCommand.toEntity()
        return userJpaRepository.save(signupRequestUserEntity).toDomain()
    }

    @Transactional(readOnly = true)
    fun findByProviderId(providerId: String): Optional<SodamUser> {
        val foundSocialUsersEntityOptionalByProviderId = socialUserJpaRepository.findByProviderId(providerId)
        if (foundSocialUsersEntityOptionalByProviderId.isEmpty) {
            return Optional.empty()
        }

        return foundSocialUsersEntityOptionalByProviderId.map { it.toDomain() }
    }

    @Transactional
    fun createSocialUser(
        name: String,
        provider: String,
        providerId: String
    ) : SodamUser {
        val socialUsersEntity = SocialUsersEntity.newEntity(
            userName = name,
            provider = provider,
            providerId = providerId,
        )
        return socialUserJpaRepository.save(socialUsersEntity).toDomain()
    }


}
