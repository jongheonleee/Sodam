package com.backend.sodam.domain.users.service

import com.backend.sodam.domain.articles.controller.response.ArticleSummaryResponse
import com.backend.sodam.domain.grades.model.GradesType
import com.backend.sodam.domain.grades.repository.UserGradeRepository
import com.backend.sodam.domain.positions.model.PositionsType
import com.backend.sodam.domain.positions.repository.UserPositionRepository
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionRepository
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.NormalUserRepository
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import com.backend.sodam.domain.users.service.command.UserSignupCommand
import com.backend.sodam.domain.users.controller.response.SocialUserResponse
import com.backend.sodam.domain.users.controller.response.UserProfileResponse
import com.backend.sodam.domain.users.controller.response.UserResponse
import com.backend.sodam.domain.users.controller.response.UserSignupResponse
import com.backend.sodam.domain.users.controller.response.UserUpdateResponse
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.command.UserUpdateCommand
import com.backend.sodam.domain.users.service.port.CreateNormalUserPort
import com.backend.sodam.domain.users.service.port.CreateSocialUserPort
import com.backend.sodam.domain.users.service.port.FetchSocialUserPort
import com.backend.sodam.domain.users.service.port.FetchUserPort
import com.backend.sodam.domain.users.service.usescase.DeleteUserUseCase
import com.backend.sodam.domain.users.service.usescase.FetchUserUseCase
import com.backend.sodam.domain.users.service.usescase.RegisterUserUseCase
import com.backend.sodam.domain.users.service.usescase.UpdateUserUseCase
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
    // 1. 회원
    // 조회용 빈 DI
    private val fetchSocialUserPort: FetchSocialUserPort, // 소셜회원의 경우, 일반 회원가 다른 조회 부분이 존재함
    private val fetchUserPorts: List<FetchUserPort>,

    // 생성용 빈 DI
    private val createNormalUserPort: CreateNormalUserPort,
    private val createSocialUserPort: CreateSocialUserPort,

    private val normalUserRepository: NormalUserRepository,
    private val userSubscriptionRepository: UserSubscriptionRepository,
    private val kakaoUserPort: KakaoUserPort,
    private val userGradeRepository: UserGradeRepository,
    private val userPositionRepository: UserPositionRepository,
): FetchUserUseCase, RegisterUserUseCase, UpdateUserUseCase, DeleteUserUseCase {

    // 밑에 두 메서드 해당 메서드 하나로 구현하기
    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class]
    )
    override fun registerNormalUser(userSignupCommand: UserSignupCommand): UserSignupResponse {
        if (isDuplicatedEmail(userSignupCommand.email))  // 이메일 중복 여부 확인
            throw UserException.UserAlreadyExistsException()

        // 일반 회원 등록처리 진행
        val sodamUser = createNormalUserPort.createUser(userSignupCommand)
        userPositionRepository.createPositionForUser(sodamUser.userId, userSignupCommand.positionId)
        userSubscriptionRepository.createSubscriptionForUser(sodamUser.userId)
        userGradeRepository.createGradeForUser(sodamUser.userId, GradesType.ENTRY.name)
        return sodamUser.toSignupResponse()
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class]
    )
    override fun registerSocialUser(socialUserSignupCommand: SocialUserSignupCommand): UserSignupResponse {
        // 소셜 회원 등록처리 진행
        val sodamUser = createSocialUserPort.createSocialUser(socialUserSignupCommand)
        userPositionRepository.createPositionForSocialUser(sodamUser.userId, PositionsType.TBD.fullName)
        userSubscriptionRepository.createUserSubscriptionForSocialUser(sodamUser.userId)
        userGradeRepository.createGradeForSocialUser(sodamUser.userId, GradesType.ENTRY.name)
        return sodamUser.toSignupResponse()
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class]
    )
    fun updateUserInfo(userId: String, userUpdateCommand: UserUpdateCommand): UserUpdateResponse {
        if (isDuplicatedEmail(userUpdateCommand.email))
            throw UserException.UserAlreadyExistsException()

        if (!isExistsByUSerId(userId))
            throw UserException.UserNotFoundException()

        val fetchPort = getFetchPortByUserId(userId)
        val target = fetchPort.findByUserId(userId).get()

        when(target.userType) {
            UserType.SOCIAL -> {
                // 기본 회원 정보를 업데이트한다.
                val updatedSodamUser = normalUserRepository.updateSocialUser(
                    socialUserId = target.userId,
                    userUpdateCommand = userUpdateCommand,
                )
                // 포지션을 재등록한다.
                userPositionRepository.upsertPositionForSocialUser(
                    socialUserId = target.userId,
                    positionId = userUpdateCommand.positionId
                )

                return UserUpdateResponse(
                    username = updatedSodamUser.username,
                    email = updatedSodamUser.email,
                    introduce = updatedSodamUser.introduce,
                    encryptedPassword = updatedSodamUser.encryptedPassword,
                )
            }

            else -> {
                val updatedSodamUser = normalUserRepository.updateNormalUser(
                    userId = target.userId,
                    userUpdateCommand = userUpdateCommand,
                )

                // 포지션을 재등록한다.
                userPositionRepository.upsertPositionForUser(
                    userId = target.userId,
                    positionId = userUpdateCommand.positionId
                )

                return UserUpdateResponse(
                    username = updatedSodamUser.username,
                    email = updatedSodamUser.email,
                    introduce = updatedSodamUser.introduce,
                    encryptedPassword = updatedSodamUser.encryptedPassword,
                )
            }
        }
    }

    fun findByEmail(email: String): UserResponse {
        val fetchPort = getFetchPortByEmail(email)
        return UserResponse.toResponse(sodamUser = fetchPort.findByEmail(email))
    }

    fun findByUserId(userId: String): UserResponse {
        val fetchPort = getFetchPortByUserId(userId)
        return UserResponse.toResponse(sodamUser = fetchPort.findByUserId(userId).get())
    }

    fun findKakaoUser(accessToken: String): SocialUserResponse {
        val foundUserFromKakao = kakaoUserPort.findUserFromKakao(accessToken)
        return SocialUserResponse(name = foundUserFromKakao.username, provider = "kakao", providerId = foundUserFromKakao.providerId)
    }

    fun findByProviderId(providerId: String): UserResponse? {
        return fetchSocialUserPort.findByProviderId(providerId) // socialUser
            .map { UserResponse.toResponse(it) }
            .orElse(null)
    }


    fun findUserProfileInfo(userId: String): UserProfileResponse {
        val fetchPort = getFetchPortByUserId(userId)
        val sodamUserDetail = fetchPort.findProfileInfo(userId).get()
        return sodamUserDetail.toResponse()
    }

    fun getOwnArticles(pageable: Pageable, userId: String): Page<ArticleSummaryResponse> {
        val fetchPort = getFetchPortByUserId(userId)
        return fetchPort.findOwnArticlesByPageBy(pageable = pageable, userId = userId)
    }

    fun getOwnLikeArticles(pageable: Pageable, userId: String): Page<ArticleSummaryResponse> {
        val fetchPort = getFetchPortByUserId(userId)
        return fetchPort.findOwnLikeArticles(pageable = pageable, userId = userId)
    }

    private fun getFetchPortByUserType(userType: UserType): FetchUserPort
        = fetchUserPorts.stream()
                        .filter { it.isTarget(userType) }
                        .findFirst()
                        .orElseThrow { IllegalArgumentException() }

    private fun getFetchPortByEmail(email: String): FetchUserPort
        = fetchUserPorts.stream()
                        .filter { it.isExistsByEmail(email) }
                        .findFirst()
                        .orElseThrow { UserException.UserNotFoundException() }
    
    private fun getFetchPortByUserId(userId: String): FetchUserPort
        = fetchUserPorts.stream()
                        .filter { it.isExistsByUserId(userId) }
                        .findFirst()
                        .orElseThrow { UserException.UserNotFoundException() }

    private fun isDuplicatedEmail(email: String): Boolean
        = fetchUserPorts.stream()
                        .anyMatch { it.isExistsByEmail(email) }

    private fun isExistsByUSerId(userId: String): Boolean
        = fetchUserPorts.stream()
                        .anyMatch { it.isExistsByUserId(userId) }

}
