package com.backend.sodam.domain.users.service

import com.backend.sodam.domain.articles.controller.response.ArticleSummaryResponse
import com.backend.sodam.domain.grades.model.GradesType
import com.backend.sodam.domain.grades.repository.UserGradeRepository
import com.backend.sodam.domain.positions.model.PositionsType
import com.backend.sodam.domain.positions.repository.NormalUserPositionRepository
import com.backend.sodam.domain.positions.service.port.UpdateUserPositionPort
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionRepository
import com.backend.sodam.domain.users.exception.UserException
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
import com.backend.sodam.domain.users.service.port.DeleteUserPort
import com.backend.sodam.domain.users.service.port.FetchSocialUserPort
import com.backend.sodam.domain.users.service.port.FetchUserPort
import com.backend.sodam.domain.users.service.port.UpdateUserPort
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
    // 1. 회원 관련 빈 DI
    private val fetchSocialUserPort: FetchSocialUserPort,
    private val fetchUserPorts: List<FetchUserPort>,
    private val createNormalUserPort: CreateNormalUserPort,
    private val createSocialUserPort: CreateSocialUserPort,
    private val updateUserPorts: List<UpdateUserPort>,
    private val deleteUserPorts: List<DeleteUserPort>,

    // 2. 그외의 연관되어 있는 포트들 DI
    private val updateUserPositionPorts: List<UpdateUserPositionPort>,
    private val userSubscriptionRepository: UserSubscriptionRepository,
    private val kakaoUserPort: KakaoUserPort,
    private val userGradeRepository: UserGradeRepository,
    private val normalUserPositionRepository: NormalUserPositionRepository,
): FetchUserUseCase, RegisterUserUseCase, UpdateUserUseCase, DeleteUserUseCase {

    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class]
    )
    override fun registerNormalUser(userSignupCommand: UserSignupCommand): UserSignupResponse {
        // 회원등록 작업 전 유효성 검증
        checkDuplicatedEmail(userSignupCommand.email)

        // 회원등록 처리
        val sodamUser = createNormalUserPort.createUser(userSignupCommand)
        normalUserPositionRepository.createPositionForUser(sodamUser.userId, userSignupCommand.positionId)
        userSubscriptionRepository.createSubscriptionForUser(sodamUser.userId)
        userGradeRepository.createGradeForUser(sodamUser.userId, GradesType.ENTRY.name)
        return sodamUser.toSignupResponse()
    }


    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class]
    )
    override fun registerSocialUser(socialUserSignupCommand: SocialUserSignupCommand): UserSignupResponse {
        // 소셜 회원 등록처리
        val sodamUser = createSocialUserPort.createSocialUser(socialUserSignupCommand)
        normalUserPositionRepository.createPositionForSocialUser(sodamUser.userId, PositionsType.TBD.fullName)
        userSubscriptionRepository.createUserSubscriptionForSocialUser(sodamUser.userId)
        userGradeRepository.createGradeForSocialUser(sodamUser.userId, GradesType.ENTRY.name)
        return sodamUser.toSignupResponse()
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class]
    )
    override fun updateUserInfo(userId: String, userUpdateCommand: UserUpdateCommand): UserUpdateResponse {
        // 회원 유형에 맞는 포트 조회
        val userType = extractUserType(userId) // 회원의 유형 추출
        val updatePort = getUpdatePortByUserType(userType) // 회원의 유형을 다룰 수 있는 포트 조회
        val updatePositionPort = getUpdatePositionPortByUserType(userType)

        // 업데이트 작업 전 유효성 검증
        checkDuplicatedEmail(userUpdateCommand.email) // 이메일 중복 여부
        checkExistsUser(userId) // 아이디 존재 여부

        // 업데이트 작업 처리
        val updatedSodamUser = updatePort.updateUserInfo(userId = userId, userUpdateCommand = userUpdateCommand) // 회원 필드 업데이트(이때, 포지션 비움)
        updatePositionPort.upsertUserPosition(userId = userId, positionId = userUpdateCommand.positionId)
        return updatedSodamUser.toUpdateResponse()
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

    // 특정 유저의 부가정보를 조회하는 추출 메서드
    private fun extractUserType(userId: String): UserType {
        val fetchPort = getFetchPortByUserId(userId)
        val sodamUser = fetchPort.findByUserId(userId).get()
        return sodamUser.userType
    }

    // 비즈니스 로직 적용전 유효성 검증 메서드
    private fun checkDuplicatedEmail(email: String) {
        if (isDuplicatedEmail(email))  // 이메일 중복 여부 확인
            throw UserException.UserAlreadyExistsException()
    }

    private fun checkExistsUser(userId: String) {
        if (!isExistsByUSerId(userId))
            throw UserException.UserNotFoundException()
    }

    // 특정 조건에 부합한 포트 조회용 메서드
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

    private fun getUpdatePortByUserType(userType: UserType): UpdateUserPort
        = updateUserPorts.stream()
                         .filter { it.isTarget(userType) }
                         .findFirst()
                         .orElseThrow { IllegalArgumentException() }

    private fun getUpdatePositionPortByUserType(userType: UserType): UpdateUserPositionPort
        = updateUserPositionPorts.stream()
                                 .filter { it.isTarget(userType) }
                                 .findFirst()
                                 .orElseThrow { IllegalArgumentException() }

}
