package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionRepository
import com.backend.sodam.domain.users.service.dto.SignupRequestDto
import com.backend.sodam.domain.users.service.dto.SignupResponse
import com.backend.sodam.domain.users.service.dto.UserResponse
import com.backend.sodam.domain.users.service.dto.toEntity
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class UserRepository(
    private val userJpaRepository: UserJpaRepository,
    private val userSubscriptionRepository: UserSubscriptionRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository
) {

    @Transactional
    fun findByUserEmail(email: String): Optional<UserResponse> {
        val foundByUserEmail = userJpaRepository.findByUserEmail(email)
        return foundByUserEmail.map {
            UserResponse(
                userId = it.userId,
                email = it.userEmail,
                name = it.userName,
                password = it.password,
                profileImage = it.userImage,
                introduce = it.userIntroduce
            )
        }
    }

    @Transactional
    fun create(signupRequestDto: SignupRequestDto): SignupResponse {
        // 회원가입 진행
        val signupRequestUserEntity = signupRequestDto.toEntity()
        val savedUserEntity = userJpaRepository.save(signupRequestUserEntity)

        return SignupResponse(
            userId = savedUserEntity.userId,
            email = savedUserEntity.userEmail,
            name = savedUserEntity.userName
        )
    }

    @Transactional
    fun findByProviderId(providerId: String): Optional<UserResponse> {
        val foundSocialUsersEntityOptionalByProviderId = socialUserJpaRepository.findByProviderId(providerId)
        if (foundSocialUsersEntityOptionalByProviderId.isEmpty) {
            return Optional.empty()
        }

        val socialUsersEntity = foundSocialUsersEntityOptionalByProviderId.get()

        return Optional.of(UserResponse(
            userId = socialUsersEntity.socialUserId,
            name = socialUsersEntity.userName,
            // 추후에 추가되야 하는 필드 -> provider, providerId, role
        ))
    }
}
