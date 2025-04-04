package com.backend.sodam.domain.positions.repository

import com.backend.sodam.domain.positions.entity.PositionsEntity
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class SocialUserPositionRepositoryIntegrationTest(
    // 테스트 대상
    private val sut: SocialUserPositionRepository,

    // 의존대상
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val positionsJpaRepository: PositionJpaRepository,

    // 테스트 환경 구축에 필요한 오브젝트
    private val userPositionsJpaRepository: UsersPositionJpaRepository
) : DescribeSpec({

    // 테스트 과정에서 사용할 목 데이터
    val mockPosition = PositionsEntity(
        positionId = UUID.randomUUID().toString(),
        positionName = "미정", // PositionsType.TBD.fullName
        ord = 1,
        validYN = 0
    )

    val mockSocialUser = SocialUsersEntity(
        socialUserId = UUID.randomUUID().toString(),
        provider = "kakao",
        providerId = UUID.randomUUID().toString()
    )

    // kotest에서 트랜잭션을 적용하려면 SpringExtension을 사용해야함
    extension(SpringExtension)

    // 테스트 환경 세팅
    beforeTest {
        // 회원 포지션 교차 테이블 비우기
        userPositionsJpaRepository.deleteAll()

        // 일반회원 테이블 비우기
        socialUserJpaRepository.deleteAll()

        // 포지션 테이블 비우기
        positionsJpaRepository.deleteAll()

        // 목 데이터 등록
        positionsJpaRepository.save(mockPosition)
        socialUserJpaRepository.save(mockSocialUser)
    }
    // 테스트 환경 정리
    afterTest {
        userPositionsJpaRepository.deleteAll()
        socialUserJpaRepository.deleteAll()
        positionsJpaRepository.deleteAll()
    }

    // 테스트 진행
    describe("소셜회원에게 positionName에 해당하는 포지션을 등록할 때") {

        it("포지션이 정상적으로 등록돼야 한다.") {
            assertDoesNotThrow {
                sut.createByPositionName(userId = mockSocialUser.socialUserId, positionName = mockPosition.positionName)
            }
        }
    }

    describe("소셜회원이 회원 정보를 수정할 때") {
        it("소셜회원에 포지션이 정상적으로 수정돼야 한다.") {
            assertDoesNotThrow {
                sut.upsertUserPosition(userId = mockSocialUser.socialUserId, positionId = mockPosition.positionId)
            }
        }
    }
})
