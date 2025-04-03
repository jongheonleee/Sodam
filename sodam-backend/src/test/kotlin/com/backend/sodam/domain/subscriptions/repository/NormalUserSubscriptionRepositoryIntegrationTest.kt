package com.backend.sodam.domain.subscriptions.repository

import com.backend.sodam.domain.subscriptions.entity.SubscriptionsEntity
import com.backend.sodam.domain.subscriptions.model.SubscriptionsType
import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class NormalUserSubscriptionRepositoryIntegrationTest(
    // 테스트 대상
    private val sut: NormalUserSubscriptionRepository,

    // 의존 대상
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val userSubscriptionJpaRepository: UserSubscriptionJpaRepository,
    private val subscriptionJpaRepository: SubscriptionJpaRepository,

    // 테스트 환경에 필요한 오브젝트
): DescribeSpec({

    val mockSubscription = SubscriptionsEntity( subscriptionId = UUID.randomUUID().toString(),
        subscriptionName = "FREE",
        subscriptionContent = "테스트용 구독권입니다.",
        viewCnt = 0,
        downCnt = 0 )

    val mockNormalUser = UsersEntity(
        userId = UUID.randomUUID().toString(),
        userEmail = "test@test.com",
        userName = "test",
        introduce = "test",
        profileImageUrl = "test",
        password = "test",
    )

    // kotest에서 트랜잭션을 적용하려면 SpringExtension을 사용해야함
    extension(SpringExtension)

    // 테스트 환경 세팅
    beforeTest {
        // 회원 포지션 교차 테이블 비우기
        userSubscriptionJpaRepository.deleteAll()

        // 일반회원 테이블 비우기
        normalUserJpaRepository.deleteAll()

        // 포지션 테이블 비우기
        subscriptionJpaRepository.deleteAll()

        // 목 데이터 등록
        subscriptionJpaRepository.save(mockSubscription)
        normalUserJpaRepository.save(mockNormalUser)

    }


    // 테스트 환경 정리
    afterTest {
        // 회원 포지션 교차 테이블 비우기
        userSubscriptionJpaRepository.deleteAll()

        // 일반회원 테이블 비우기
        normalUserJpaRepository.deleteAll()

        // 구독권 테이블 비우기
        subscriptionJpaRepository.deleteAll()
    }


    describe("사용자가 일반회원으로 회원가입할 때") {

        it("무료 구독권을 정상 발급한다.") {
            sut.createSubscription(mockNormalUser.userId, SubscriptionsType.FREE)
        }

    }

    describe("일반회원의 아이디로 해당 회원의 구독권을 조회할 때") {

        it("발급되어 있으며 유효한 구독권을 조회한다.") {

        }
    }
})
