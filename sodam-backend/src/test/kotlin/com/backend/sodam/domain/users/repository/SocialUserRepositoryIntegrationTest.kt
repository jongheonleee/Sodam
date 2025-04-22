package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.grades.entity.GradesEntity
import com.backend.sodam.domain.grades.model.GradesType
import com.backend.sodam.domain.grades.repository.GradesJpaRepository
import com.backend.sodam.domain.grades.repository.SocialUserGradeRepository
import com.backend.sodam.domain.grades.repository.UserGradeJpaRepository
import com.backend.sodam.domain.positions.entity.PositionsEntity
import com.backend.sodam.domain.positions.model.PositionsType
import com.backend.sodam.domain.positions.repository.PositionJpaRepository
import com.backend.sodam.domain.positions.repository.SocialUserPositionRepository
import com.backend.sodam.domain.positions.repository.UsersPositionJpaRepository
import com.backend.sodam.domain.subscriptions.entity.SubscriptionsEntity
import com.backend.sodam.domain.subscriptions.model.SubscriptionsType
import com.backend.sodam.domain.subscriptions.repository.SocialUserSubscriptionRepository
import com.backend.sodam.domain.subscriptions.repository.SubscriptionJpaRepository
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionJpaRepository
import com.backend.sodam.domain.tokens.repository.TokenJpaRepository
import com.backend.sodam.domain.tokens.repository.TokenRepositoryForSocialUser
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.SodamUserDetail
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import com.backend.sodam.domain.users.service.command.UserUpdateCommand
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest // 현재 스프링 컨테이너 업로드해서 서비스에서 사용하는 빈들을 모두 관리해서 테스트 코드를 구동하고 있음 -> 이 부분 추후에 효율적으로 구성하기
class SocialUserRepositoryIntegrationTest(
    // - 테스트 대상
    private val sut: SocialUserRepository,

    // - 의존하고 있는 오브젝트
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val socialUserPositionRepository: SocialUserPositionRepository,
    private val socialUserGradeRepository: SocialUserGradeRepository,
    private val socialUserSubscriptionRepository: SocialUserSubscriptionRepository,

    // 테스트 환경 구축에 필요한 오브젝트
    // - 1. 기본적으로 세팅되어야 하는 데이터
    private val gradesJpaRepository: GradesJpaRepository,
    private val positionsJpaRepository: PositionJpaRepository,
    private val subscriptionJpaRepository: SubscriptionJpaRepository,
    private val tokenJpaRepository: TokenJpaRepository,

    // - 2. 회원과 연관된 교차 테이블
    private val userGradeJpaRepository: UserGradeJpaRepository,
    private val userPositionsJpaRepository: UsersPositionJpaRepository,
    private val userSubscriptionJpaRepository: UserSubscriptionJpaRepository
) : DescribeSpec({

    // 테스트 과정에서 사용할 목 데이터
    val mockPosition = PositionsEntity(
        positionId = UUID.randomUUID().toString(),
        positionName = "미정", // PositionsType.TBD.fullName
        ord = 1,
        validYN = 0
    )
    val mockSubscription = SubscriptionsEntity(
        subscriptionId = UUID.randomUUID().toString(),
        subscriptionName = "FREE",
        subscriptionContent = "테스트용 구독권입니다.",
        viewCnt = 0,
        downCnt = 0
    )
    val mockGrade = GradesEntity(
        gradeId = UUID.randomUUID().toString(),
        gradeName = "ENTRY",
        gradeOrd = 1,
        gradeSummary = "테스트용 등급 데이터입니다.",
        gradeDescription = "테스트용 등급데이터입니다.",
        validYN = 0
    )

    // kotest에서 트랜잭션을 적용하려면 SpringExtension을 사용해야함
    extension(SpringExtension)

    // 테스트 환경 세팅
    beforeTest {
        // 회원 가입 등 회원 처리 로직에 필요한 기본 데이터 세팅
        // 회원과 관련된 테이블
        userPositionsJpaRepository.deleteAll()
        userGradeJpaRepository.deleteAll()
        userSubscriptionJpaRepository.deleteAll()
        tokenJpaRepository.deleteAll()

        // 일반회원 및 소셜회원 테이블 비우기
        socialUserJpaRepository.deleteAll()

        // 테이블 초기화 되었는지 확인
        socialUserJpaRepository.count().shouldBe(0)

        // 회원가입 처리 등에서 요구되는 기본 데이터 셋 추가 - 1. 포지션, 2. 구독권, 3. 등급
        positionsJpaRepository.save(mockPosition)
        gradesJpaRepository.save(mockGrade)
        subscriptionJpaRepository.save(mockSubscription)
    }

    // 테스트 환경 정리
    afterTest {
        // 테스트 DB 정리하기
        userPositionsJpaRepository.deleteAll()
        userGradeJpaRepository.deleteAll()
        userSubscriptionJpaRepository.deleteAll()
        tokenJpaRepository.deleteAll()

        // 일반회원 및 소셜회원 테이블 비우기
        socialUserJpaRepository.deleteAll()

        // 테이블 초기화 되었는지 확인
        socialUserJpaRepository.count().shouldBe(0)
    }

    context("소셜회원 providerId로 조회할 때") {
        val command = SocialUserSignupCommand(
            username = "테스트 유저",
            provider = "kakao",
            providerId = UUID.randomUUID().toString()
        )

        it("해당 providerId관련 소셜회원이 존재한다면, true를 반환한다.") {
            sut.createSocialUser(command) // 이 부분도 옮길 것
            val optional = sut.findEntityByProviderId(command.providerId)
            optional.isPresent shouldBe true

            val actual = optional.get()
            val expected = SodamUser(
                provider = command.provider,
                username = command.username,
                providerId = command.providerId
            )

            actual.provider shouldBe expected.provider
            actual.providerId shouldBe expected.providerId
            actual.userName shouldBe expected.username
        }

        it("해당 providerId관련 소셜회원이 존재하지 않는다면, false를 반환한다.") {
            val notExistsProviderId = "dwadwadaw"
            sut.createSocialUser(command)
            val optional = sut.findEntityByProviderId(notExistsProviderId)
            optional.isEmpty shouldBe true
        }
    }

    context("소셜회원 등록할 때") {
        val command = SocialUserSignupCommand(
            username = "테스트 유저",
            provider = "kakao",
            providerId = UUID.randomUUID().toString()
        )

        it("소셜회원을 성공적으로 등록한다") {
            val actual = sut.createSocialUser(command)
            val expected = SodamUser(
                provider = command.provider,
                username = command.username,
                providerId = command.providerId
            )

            actual.provider shouldBe expected.provider
            actual.providerId shouldBe expected.providerId
            actual.username shouldBe expected.username
        }
    }

    context("소셜회원 업데이트할 때") {
        val command = SocialUserSignupCommand(
            username = "테스트 유저",
            provider = "kakao",
            providerId = UUID.randomUUID().toString()
        )

        it("업데이트 데이터와 userId를 제대로 전달했으면, 정상적으로 업데이트가 되야한다.") {
            val target = sut.createSocialUser(command)

            val socialUserId = target.userId
            val updateCommand = UserUpdateCommand(
                email = "update@test.com",
                name = "홍길동",
                encryptedPassword = "asdf1234",
                positionId = mockPosition.positionId,
                introduce = "업데이트했습니다."
            )

            val actual = sut.updateUserInfo(
                userId = socialUserId,
                userUpdateCommand = updateCommand
            )
            val expected = SodamUser(
                userId = socialUserId,
                email = updateCommand.email,
                username = updateCommand.name,
                introduce = updateCommand.introduce
            )

            // 내용 비교
            actual.userId shouldBe expected.userId
            actual.email shouldBe expected.email
            actual.username shouldBe expected.username
            actual.introduce shouldBe expected.introduce
            actual.encryptedPassword shouldBe expected.encryptedPassword
        }
    }

    context("소셜회원 프로필 정보 조회할 때") {
        // 소셜회원 등록
        // - 소셜회원, 포지션, 등급, 구독권
        val command = SocialUserSignupCommand(
            username = "테스트 유저",
            provider = "kakao",
            providerId = UUID.randomUUID().toString()
        )

        it("소셜회원이 정상적으로 등록되었다면, 회원 정보, 포지션, 등급, 구독권을 정상적으로 조회해야한다.") {
            val target = sut.createSocialUser(command)
            socialUserPositionRepository.createByPositionName(target.userId, PositionsType.TBD.fullName)
            socialUserGradeRepository.createGrade(target.userId, GradesType.ENTRY)
            socialUserSubscriptionRepository.createSubscription(target.userId, SubscriptionsType.FREE)

            val optional = sut.findProfileInfo(target.userId)
            optional.isPresent shouldBe true

            val actual = optional.get()

            val subscription = socialUserSubscriptionRepository.findByUserId(target.userId)

            val positionByPositionId = positionsJpaRepository.findByPositionName(PositionsType.TBD.fullName)
            positionByPositionId.isPresent shouldBe true
            val position = positionByPositionId.get()

            val gradeByName = gradesJpaRepository.findByGradeName(GradesType.ENTRY.name)
            gradeByName.isPresent shouldBe true
            val grade = gradeByName.get()

            val expected = SodamUserDetail(
                userId = target.userId,
                name = target.username,
                email = target.email,
                introduce = target.introduce,
                profileImageUrl = target.profileImageUrl,
                subscription = subscription.subscriptionType,
                positions = listOf(position.positionName),
                articleTotalCnt = 0,
                grade = grade.gradeName,
                ranking = 1 // 랭킹부분 추후에 처리하기
            )

            actual.userId shouldBe expected.userId
            actual.email shouldBe expected.email
            actual.name shouldBe expected.name
            actual.introduce shouldBe expected.introduce
            actual.subscription shouldBe expected.subscription
            actual.positions[0] shouldBe expected.positions[0]
            actual.articleTotalCnt shouldBe expected.articleTotalCnt
            actual.grade shouldBe expected.grade
        }
    }

    // 밑에 부분은 여기서 다루면 테스트 코드가 너무 엉켜버릴 것 같기에 Article 부분에서 다루기
    context("소셜회원 자신이 작성한 게시글 조회할 때") {
        // 소셜회원 등록(단순 등록)
        // 게시글 3개 등록

        it("소셜회원이 게시글 3개를 작성한 경우, 해당 게시글을 조회할 수 있어야한다.") {
        }
    }

    context("소셜회원 좋아요 게시글 조회할 때") {

        it("소셜회원의 좋아요 게시글이 3개인 경우, 해당 게시글을 조회할 수 있어야한다.") {
        }
    }
})
