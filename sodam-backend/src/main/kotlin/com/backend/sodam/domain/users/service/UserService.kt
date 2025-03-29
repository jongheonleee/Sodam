package com.backend.sodam.domain.users.service

import com.backend.sodam.domain.articles.controller.response.ArticleSummaryResponse
import com.backend.sodam.domain.grades.model.GradesType
import com.backend.sodam.domain.grades.repository.UserGradeRepository
import com.backend.sodam.domain.positions.model.PositionsType
import com.backend.sodam.domain.positions.repository.UserPositionRepository
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionRepository
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.UserRepository
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import com.backend.sodam.domain.users.service.command.UserSignupCommand
import com.backend.sodam.domain.users.controller.response.SimpleUserResponse
import com.backend.sodam.domain.users.controller.response.SocialUserResponse
import com.backend.sodam.domain.users.controller.response.UserProfileResponse
import com.backend.sodam.domain.users.controller.response.UserResponse
import com.backend.sodam.domain.users.controller.response.UserSignupResponse
import com.backend.sodam.global.port.KakaoUserPort
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class UserService(
    private val userRepository: UserRepository,
    private val userSubscriptionRepository: UserSubscriptionRepository,
    private val kakaoUserPort: KakaoUserPort,
    private val userGradeRepository: UserGradeRepository,
    private val userPositionRepository: UserPositionRepository,
) {

    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class]
    )
    fun signupUser(userSignupCommand: UserSignupCommand): UserSignupResponse {
        if (userRepository.isExistsByEmail(userSignupCommand.email)) {
            throw UserException.UserAlreadyExistsException()
        }

        val sodamUser = userRepository.create(userSignupCommand)
        userPositionRepository.createPositionForUser(sodamUser.userId, userSignupCommand.positionId)
        userSubscriptionRepository.createSubscriptionForUser(sodamUser.userId)
        userGradeRepository.createGradeForUser(sodamUser.userId, GradesType.ENTRY.name)
        return sodamUser.toSignupResponse()
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class]
    )
    fun signupSocialUser(socialUserSignupCommand: SocialUserSignupCommand): UserSignupResponse {
        if (userRepository.isExistsByProviderId(socialUserSignupCommand.providerId)) {
            throw UserException.SocialUserAlreadyExistsException()
        }

        val sodamUser = userRepository.createSocialUser(socialUserSignupCommand)
        userPositionRepository.createPositionForSocialUser(sodamUser.userId, PositionsType.TBD.fullName)
        userSubscriptionRepository.createUserSubscriptionForSocialUser(sodamUser.userId)
        userGradeRepository.createGradeForSocialUser(sodamUser.userId, GradesType.ENTRY.name)
        return sodamUser.toSignupResponse()
    }

    fun findByUserEmail(email: String): SimpleUserResponse {
        val foundOptionalSodamUserByEmail = userRepository.findByUserEmail(email)
        if (foundOptionalSodamUserByEmail.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val sodamUser = foundOptionalSodamUserByEmail.get()

        return SimpleUserResponse(
            username = sodamUser.username,
            email = sodamUser.email
        )
    }

    fun findByUserId(userId: String): UserResponse {
        val foundOptionalSodamUserByUserId = userRepository.findByUserId(userId)
        if (foundOptionalSodamUserByUserId.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val sodamUser = foundOptionalSodamUserByUserId.get()
        return UserResponse.toUserResponse(
            sodamUser = sodamUser
        )
    }

    fun findKakaoUser(accessToken: String): SocialUserResponse {
        val foundUserFromKakao = kakaoUserPort.findUserFromKakao(accessToken)
        return SocialUserResponse(
            name = foundUserFromKakao.username,
            provider = "kakao",
            providerId = foundUserFromKakao.providerId
        )
    }

    fun findByProviderId(providerId: String): UserResponse? {
        return userRepository.findSocialUserByProviderId(providerId) // socialUser
            .map { UserResponse.toUserResponse(it) }
            .orElse(null)
    }

    fun findByEmail(email: String): UserResponse? {
        return userRepository.findByUserEmail(email)
            .map { UserResponse.toUserResponse(it) }
            .orElse(null)
    }

    fun findUserProfileInfo(userId: String): UserProfileResponse {
        val sodamUserDetailOptional = userRepository.findProfileInfo(userId)
        if (sodamUserDetailOptional.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val sodamUserDetail = sodamUserDetailOptional.get()
        return sodamUserDetail.toResponse()
    }

    fun getOwnArticles(pageable: Pageable, userId: String): Page<ArticleSummaryResponse> {
        return userRepository.findOwnArticlesByPageBy(
            pageable = pageable,
            userId = userId
        )
    }

    fun getOwnLikeArticles(pageable: Pageable, userId: String): Page<ArticleSummaryResponse> {
        return userRepository.findOwnLikeArticles(
            pageable = pageable,
            userId = userId
        )
    }
}
