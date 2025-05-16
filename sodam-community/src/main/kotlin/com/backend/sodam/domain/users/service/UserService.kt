package com.backend.sodam.domain.users.service

import com.backend.sodam.domain.articles.service.response.ArticleSummaryResponse
import com.backend.sodam.domain.grades.exception.GradeException
import com.backend.sodam.domain.grades.model.GradesType
import com.backend.sodam.domain.grades.service.port.CreateUserGradePort
import com.backend.sodam.domain.grades.service.port.FetchGradePort
import com.backend.sodam.domain.positions.exception.PositionException
import com.backend.sodam.domain.positions.model.PositionsType
import com.backend.sodam.domain.positions.service.port.CreateUserPositionPort
import com.backend.sodam.domain.positions.service.port.FetchPositionPort
import com.backend.sodam.domain.positions.service.port.UpdateUserPositionPort
import com.backend.sodam.domain.subscriptions.exception.SubscriptionException
import com.backend.sodam.domain.subscriptions.model.SubscriptionsType
import com.backend.sodam.domain.subscriptions.service.port.CreateUserSubscriptionPort
import com.backend.sodam.domain.subscriptions.service.port.FetchSubscriptionPort
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import com.backend.sodam.domain.users.service.command.UserSignupCommand
import com.backend.sodam.domain.users.service.command.UserUpdateCommand
import com.backend.sodam.domain.users.service.port.CreateNormalUserPort
import com.backend.sodam.domain.users.service.port.CreateSocialUserPort
import com.backend.sodam.domain.users.service.port.FetchSocialUserPort
import com.backend.sodam.domain.users.service.port.FetchUserPort
import com.backend.sodam.domain.users.service.port.UpdateUserPort
import com.backend.sodam.domain.users.service.response.SocialUserResponse
import com.backend.sodam.domain.users.service.response.UserProfileResponse
import com.backend.sodam.domain.users.service.response.UserResponse
import com.backend.sodam.domain.users.service.response.UserSignupResponse
import com.backend.sodam.domain.users.service.response.UserUpdateResponse
import com.backend.sodam.domain.users.service.usescase.DeleteUserUseCase
import com.backend.sodam.domain.users.service.usescase.FetchUserUseCase
import com.backend.sodam.domain.users.service.usescase.RegisterUserUseCase
import com.backend.sodam.domain.users.service.usescase.UpdateUserUseCase
import com.backend.sodam.global.port.KakaoUserPort
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
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

    // 2. 그외의 연관되어 있는 포트들 DI
    // - 1. 포지션
    private val fetchPositionPort: FetchPositionPort,
    private val createUserPositionPorts: List<CreateUserPositionPort>,
    private val updateUserPositionPorts: List<UpdateUserPositionPort>,

    // - 2. 구독권
    private val fetchSubscriptionPort: FetchSubscriptionPort,
    private val createUserSubscriptionPorts: List<CreateUserSubscriptionPort>,

    // - 3. 등급
    private val fetchGradePort: FetchGradePort,
    private val createUserGradePorts: List<CreateUserGradePort>,

    // - 4. 그외 시스템 외부 포트
    private val kakaoUserPort: KakaoUserPort
) : FetchUserUseCase, RegisterUserUseCase, UpdateUserUseCase, DeleteUserUseCase {

    // 📌 실제 핵심 비즈니스 로직
    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class]
    )
    override fun registerNormalUser(userSignupCommand: UserSignupCommand): UserSignupResponse {
        // 회원등록 작업 전 유효성 검증
        checkDuplicatedEmail(userSignupCommand.email)
        checkExistsPosition(userSignupCommand.positionId)
        checkExistsSubscription(SubscriptionsType.FREE.name)
        checkExistsGrade(GradesType.ENTRY.name)

        // 회원유형에 맞는 포트 조회 - 일반회원
        val userPositionCreatePort = getCreateUserPositionPortByUserType(UserType.NORMAL)
        val userSubscriptionCreatePort = getCreateUserSubscriptionPort(UserType.NORMAL)
        val userGraderCreatePort = getCreateUserGradePort(UserType.NORMAL)

        // 회원등록 처리 비즈니스 로직
        val sodamUser = createNormalUserPort.createUser(userSignupCommand)
        userPositionCreatePort.createByPositionId(userId = sodamUser.userId, positionId = userSignupCommand.positionId)
        userSubscriptionCreatePort.createSubscription(userId = sodamUser.userId, subscriptionType = SubscriptionsType.FREE)
        userGraderCreatePort.createGrade(userId = sodamUser.userId, gradeType = GradesType.ENTRY)
        return sodamUser.toSignupResponse()
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class]
    )
    override fun registerSocialUser(socialUserSignupCommand: SocialUserSignupCommand): UserSignupResponse {
        // 회원등록 작업 전 유효성 검증
        checkExistsPositionByName(PositionsType.TBD.fullName)
        checkExistsSubscription(SubscriptionsType.FREE.name)
        checkExistsGrade(GradesType.ENTRY.name)

        // 회원유형에 맞는 포트 조회 - 소셜회원
        val userPositionCreatePort = getCreateUserPositionPortByUserType(UserType.SOCIAL)
        val userSubscriptionCreatePort = getCreateUserSubscriptionPort(UserType.SOCIAL)
        val userGradeCreatePort = getCreateUserGradePort(UserType.SOCIAL)

        // 소셜 회원 등록 비즈니스 로직
        val sodamUser = createSocialUserPort.createSocialUser(socialUserSignupCommand)
        userPositionCreatePort.createByPositionName(userId = sodamUser.userId, positionName = PositionsType.TBD.fullName)
        userSubscriptionCreatePort.createSubscription(userId = sodamUser.userId, subscriptionType = SubscriptionsType.FREE)
        userGradeCreatePort.createGrade(userId = sodamUser.userId, gradeType = GradesType.ENTRY)
        return sodamUser.toSignupResponse()
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class],
        isolation = Isolation.READ_COMMITTED
    )
    override fun updateUserInfo(userId: String, userUpdateCommand: UserUpdateCommand): UserUpdateResponse {
        // 업데이트 작업 전 유효성 검증
        checkDuplicatedEmail(userUpdateCommand.email) // 이메일 중복 여부
        checkExistsUser(userId) // 아이디 존재 여부
        checkExistsPosition(userUpdateCommand.positionId) // 포지션 존재 여부

        // 회원 유형에 맞는 포트 조회 -> 일반회원, 소셜회원
        val userType = extractUserType(userId) // 회원의 유형 추출
        val updateUserPort = getUpdatePortByUserType(userType) // 회원의 유형을 다룰 수 있는 포트 조회
        val updatePositionPort = getUpdatePositionPortByUserType(userType)

        // 업데이트 작업 비즈니스 로직
        val updatedSodamUser = updateUserPort.updateUserInfo(userId = userId, userUpdateCommand = userUpdateCommand) // 회원 필드 업데이트(이때, 포지션 비움)
        updatePositionPort.upsertUserPosition(userId = userId, positionId = userUpdateCommand.positionId)
        return updatedSodamUser.toUpdateResponse()
    }

    override fun findByEmail(email: String): UserResponse {
        val fetchPort = getFetchPortByEmail(email)
        return UserResponse.toResponse(sodamUser = fetchPort.findByEmail(email))
    }

    override fun findByUserId(userId: String): UserResponse {
        val fetchPort = getFetchPortByUserId(userId)
        return UserResponse.toResponse(sodamUser = fetchPort.findByUserId(userId).get())
    }

    override fun findKakaoUser(accessToken: String): SocialUserResponse {
        val foundUserFromKakao = kakaoUserPort.findUserFromKakao(accessToken)
        return SocialUserResponse(name = foundUserFromKakao.username, provider = "kakao", providerId = foundUserFromKakao.providerId)
    }

    override fun findByProviderId(providerId: String): UserResponse? {
        return fetchSocialUserPort.findByProviderId(providerId) // socialUser
            .map { UserResponse.toResponse(it) }
            .orElse(null)
    }

    @Transactional(
        readOnly = true,
        isolation = Isolation.READ_COMMITTED
    )
    override fun findUserProfileInfo(userId: String): UserProfileResponse {
        val fetchPort = getFetchPortByUserId(userId)
        val sodamUserDetail = fetchPort.findProfileInfo(userId).get()
        return sodamUserDetail.toResponse()
    }

    @Transactional(
        readOnly = true,
        isolation = Isolation.READ_COMMITTED
    )
    override fun getOwnArticles(pageable: Pageable, userId: String): Page<ArticleSummaryResponse> {
        val fetchPort = getFetchPortByUserId(userId)
        return fetchPort.findOwnArticlesByPageBy(pageable = pageable, userId = userId)
    }

    @Transactional(
        readOnly = true,
        isolation = Isolation.READ_COMMITTED
    )
    override fun getOwnLikeArticles(pageable: Pageable, userId: String): Page<ArticleSummaryResponse> {
        val fetchPort = getFetchPortByUserId(userId)
        return fetchPort.findOwnLikeArticles(pageable = pageable, userId = userId)
    }

    // 📌 특정 유저의 부가정보를 조회하는 추출 메서드
    private fun extractUserType(userId: String): UserType {
        val fetchPort = getFetchPortByUserId(userId)
        val sodamUser = fetchPort.findByUserId(userId).get()
        return sodamUser.userType
    }

    // 📌 비즈니스 로직 적용전 유효성 검증 메서드
    private fun checkDuplicatedEmail(email: String) {
        if (isDuplicatedEmail(email)) {
            // 이메일 중복 여부 확인
            throw UserException.UserAlreadyExistsException()
        }
    }

    private fun checkExistsUser(userId: String) {
        if (!isExistsByUserId(userId)) {
            throw UserException.UserNotFoundException()
        }
    }

    private fun checkExistsPosition(positionId: String) {
        if (!isExistsPositionByPositionId(positionId)) {
            throw PositionException.PositionNotFoundException()
        }
    }

    private fun checkExistsPositionByName(positionName: String) {
        if (!isExistsPositionByPositionName(positionName)) {
            throw PositionException.PositionNotFoundException()
        }
    }

    private fun checkExistsSubscription(subscriptionName: String) {
        if (!isExistsSubscriptionByName(subscriptionName)) {
            throw SubscriptionException.SubscriptionNotFoundException()
        }
    }

    private fun checkExistsGrade(gradeName: String) {
        if (!isExistsGradeByName(gradeName)) {
            throw GradeException.GradeNotFoundException()
        }
    }

    private fun isDuplicatedEmail(email: String): Boolean =
        fetchUserPorts.stream()
            .anyMatch { it.isExistsByEmail(email) }

    private fun isExistsByUserId(userId: String): Boolean =
        fetchUserPorts.stream()
            .anyMatch { it.isExistsByUserId(userId) }

    private fun isExistsPositionByPositionId(positionId: String): Boolean =
        fetchPositionPort.isExistsByPositionId(positionId)

    private fun isExistsPositionByPositionName(positionName: String): Boolean =
        fetchPositionPort.isExistsByPositionName(positionName)

    private fun isExistsSubscriptionByName(subscriptionName: String): Boolean =
        fetchSubscriptionPort.isExistsBySubscriptionName(subscriptionName)

    private fun isExistsGradeByName(subscriptionName: String): Boolean =
        fetchGradePort.isExistsByGradeName(subscriptionName)

    // 📌 특정 조건에 부합한 포트 조회용 메서드 - 런타임 시점에 특정 비즈니스 로직을 처리할 수 있는 빈을 선택하는 메서드
    private fun getFetchPortByEmail(email: String): FetchUserPort =
        fetchUserPorts.stream()
            .filter { it.isExistsByEmail(email) }
            .findFirst()
            .orElseThrow { UserException.UserNotFoundException() }

    private fun getFetchPortByUserId(userId: String): FetchUserPort =
        fetchUserPorts.stream()
            .filter { it.isExistsByUserId(userId) }
            .findFirst()
            .orElseThrow { UserException.UserNotFoundException() }

    private fun getUpdatePortByUserType(userType: UserType): UpdateUserPort =
        updateUserPorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getUpdatePositionPortByUserType(userType: UserType): UpdateUserPositionPort =
        updateUserPositionPorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getCreateUserPositionPortByUserType(userType: UserType): CreateUserPositionPort =
        createUserPositionPorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getCreateUserSubscriptionPort(userType: UserType): CreateUserSubscriptionPort =
        createUserSubscriptionPorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getCreateUserGradePort(userType: UserType): CreateUserGradePort =
        createUserGradePorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }
}
