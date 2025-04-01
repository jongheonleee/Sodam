package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.grades.entity.GradesEntity
import com.backend.sodam.domain.grades.repository.GradesJpaRepository
import com.backend.sodam.domain.grades.repository.UserGradeJpaRepository
import com.backend.sodam.domain.positions.entity.PositionsEntity
import com.backend.sodam.domain.positions.repository.PositionJpaRepository
import com.backend.sodam.domain.positions.repository.UserPositionJpaRepository
import com.backend.sodam.domain.subscriptions.entity.SubscriptionsEntity
import com.backend.sodam.domain.subscriptions.repository.SubscriptionJpaRepository
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionJpaRepository
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionRepository
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest // 현재 스프링 컨테이너 업로드해서 서비스에서 사용하는 빈들을 모두 관리해서 테스트 코드를 구동하고 있음 -> 이 부분 추후에 효율적으로 구성하기
class SocialUserRepositoryIntegrationTest(
    // - 테스트 대상
    private val sut: SocialUserRepository,
    private val sut2: NormalUserRepository,

    // - 의존하고 있는 오브젝트
    private val userJpaRepository: NormalUserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val userSubscriptionRepository: UserSubscriptionRepository,

    // 테스트 환경 구축에 필요한 오브젝트
    // - 1. 기본적으로 세팅되어야 하는 데이터
    private val gradesJpaRepository: GradesJpaRepository,
    private val positionsJpaRepository: PositionJpaRepository,
    private val subscriptionJpaRepository: SubscriptionJpaRepository,

    // - 2. 회원과 연관된 교차 테이블
    private val userGradeJpaRepository: UserGradeJpaRepository,
    private val userPositionsJpaRepository: UserPositionJpaRepository,
    private val userSubscriptionJpaRepository: UserSubscriptionJpaRepository,
): DescribeSpec( {

    // 테스트 과정에서 사용할 목 데이터
    val mockPosition = PositionsEntity( positionId = UUID.randomUUID().toString(),
        positionName = "미정", // PositionsType.TBD.fullName
        ord = 1,
        validYN = 0 )
    val mockSubscription = SubscriptionsEntity( subscriptionId = UUID.randomUUID().toString(),
        subscriptionName = "FREE",
        subscriptionContent = "테스트용 구독권입니다.",
        viewCnt = 0,
        downCnt = 0 )
    val mockGrade = GradesEntity( gradeId = UUID.randomUUID().toString(),
        gradeName = "ENTRY",
        gradeOrd = 1,
        gradeSummary = "테스트용 등급 데이터입니다.",
        gradeDescription = "테스트용 등급데이터입니다.",
        validYN = 0 )

    // kotest에서 트랜잭션을 적용하려면 SpringExtension을 사용해야함
    extension(SpringExtension)

    // 테스트 환경 세팅
    beforeTest {
        // 회원 가입 등 회원 처리 로직에 필요한 기본 데이터 세팅
        // 회원과 관련된 테이블
        userPositionsJpaRepository.deleteAll()
        userGradeJpaRepository.deleteAll()
        userSubscriptionJpaRepository.deleteAll()

        // 일반회원 및 소셜회원 테이블 비우기
        userJpaRepository.deleteAll()
        socialUserJpaRepository.deleteAll()


        // 테이블 초기화 되었는지 확인
        userJpaRepository.count().shouldBe(0)
        socialUserJpaRepository.count().shouldBe(0)

        // 회원가입 처리 등에서 요구되는 기본 데이터 셋 추가 - 1. 포지션, 2. 구독권, 3. 등급
        positionsJpaRepository.save(mockPosition)
        gradesJpaRepository.save(mockGrade)
        subscriptionJpaRepository.save(mockSubscription)
    }


    // 테스트 환경 정리
    afterTest {
        // 테스트 DB 정리하기
    }

    context("소셜회원 providerId로 조회할 때") {
        val command = SocialUserSignupCommand(  username = "테스트 유저",
            provider = "kakao",
            providerId = UUID.randomUUID().toString())

        it("해당 providerId관련 소셜회원이 존재한다면, true를 반환한다.") {
            sut2.createSocialUser(command) // 이 부분도 옮길 것
            val optional = sut.findByProviderId(command.providerId)
            optional.isPresent shouldBe true


            val actual = optional.get()
            val expected = SodamUser(
                provider = command.provider,
                username = command.username,
                providerId = command.providerId,
            )

            actual.provider shouldBe expected.provider
            actual.providerId shouldBe expected.providerId
            actual.userName shouldBe expected.username
        }

        it("해당 providerId관련 소셜회원이 존재하지 않는다면, false를 반환한다.") {
            val notExistsProviderId = "dwadwadaw"
            sut2.createSocialUser(command)
            val optional = sut.findByProviderId(notExistsProviderId)
            optional.isEmpty shouldBe true
        }
    }


})