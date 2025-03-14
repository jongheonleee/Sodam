package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.users.entity.SocialUsersEntity
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class SocialUserRepository(
    private val socialUserJpaRepository: SocialUserJpaRepository,
) {

    @Transactional
    fun findByProviderId(providerId: String): Optional<SocialUsersEntity> {
        return socialUserJpaRepository.findByProviderId(providerId)
    }
}