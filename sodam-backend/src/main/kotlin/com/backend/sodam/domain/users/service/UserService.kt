package com.backend.sodam.domain.users.service

import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionRepository
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.UserRepository
import com.backend.sodam.domain.users.service.command.*
import com.backend.sodam.domain.users.service.response.SimpleUserResponse
import com.backend.sodam.domain.users.service.response.SocialUserResponse
import com.backend.sodam.domain.users.service.response.UserResponse
import com.backend.sodam.domain.users.service.response.UserSignupResponse
import com.backend.sodam.global.port.KakaoUserPort
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class UserService(
    private val userRepository: UserRepository,
    private val userSubscriptionRepository: UserSubscriptionRepository,
    private val kakaoUserPort: KakaoUserPort,
) {

    // - 중복 이메일을 확인한다.
    //    - 중복된 이메일이 존재하는 경우, 알림 메시지 반환
    // - 회원가입 처리를 한다.
    // - 무료 구독권을 생성한다.
    fun signupUser(userSignupCommand: UserSignupCommand): UserSignupResponse {
        userRepository.findByUserEmail(userSignupCommand.email)
            .ifPresent { throw UserException.UserAlreadyExistsException() }

        val sodamUser = userRepository.create(userSignupCommand)
        userSubscriptionRepository.createSubscriptionForUser(sodamUser.userId)

        return UserSignupResponse(
            username = sodamUser.username,
            encryptedPassword = sodamUser.encryptedPassword,
            email = sodamUser.email,
            introduce = sodamUser.introduce,
        )
    }

    fun signupSocialUser(socialUserSignupCommand: SocialUserSignupCommand): UserSignupResponse {
        userRepository.findByProviderId(socialUserSignupCommand.providerId)
            .ifPresent { throw UserException.SocialUserAlreadyExistsException() }

        val sodamUser = userRepository.createSocialUser(
            name = socialUserSignupCommand.username,
            provider = socialUserSignupCommand.provider,
            providerId = socialUserSignupCommand.providerId,
        )

        userSubscriptionRepository.createUserSubscriptionForSocialUser(sodamUser.userId)

        return UserSignupResponse(
            username = sodamUser.username
        )
    }

    fun findByUserEmail(email: String): SimpleUserResponse {
        val foundOptionalSodamUserByEmail = userRepository.findByUserEmail(email)
        if (foundOptionalSodamUserByEmail.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val sodamUser = foundOptionalSodamUserByEmail.get()

        return SimpleUserResponse(
            username = sodamUser.username,
            email = sodamUser.email,
        )
    }

    fun findKakaoUser(accessToken: String): SocialUserResponse {
        val foundUserFromKakao = kakaoUserPort.findUserFromKakao(accessToken)
        return SocialUserResponse(
                name = foundUserFromKakao.username,
                provider = "kakao",
                providerId = foundUserFromKakao.providerId,
        )
    }

    fun findByProviderId(providerId: String): UserResponse? {
        return userRepository.findByProviderId(providerId)
                             .map { UserResponse.toUserResponse(it) }
                             .orElse(null)
    }

}
