package com.backend.sodam.domain.positions.repository

import com.backend.sodam.domain.positions.entity.PositionsEntity
import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class NormalUserPositionRepositoryIntegrationTest(
    // 테스트 대상
    private val sut: NormalUserPositionRepository,

    // 의존 대상
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val positionsJpaRepository: PositionJpaRepository,

    // 테스트 환경 구축에 필요한 오브젝트
    private val userPositionsJpaRepository: UsersPositionJpaRepository,
) : DescribeSpec({

    // 테스트 과정에서 사용할 목 데이터
    val mockPosition = PositionsEntity( positionId = UUID.randomUUID().toString(),
        positionName = "미정", // PositionsType.TBD.fullName
        ord = 1,
        validYN = 0 )
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
        userPositionsJpaRepository.deleteAll()

        // 일반회원 테이블 비우기
        normalUserJpaRepository.deleteAll()

        // 포지션 테이블 비우기
        positionsJpaRepository.deleteAll()

        // 목 데이터 등록
        positionsJpaRepository.save(mockPosition)
        normalUserJpaRepository.save(mockNormalUser)
    }

    // 테스트 환경 정리
    afterTest {
        // 회원 포지션 교차 테이블 비우기
        userPositionsJpaRepository.deleteAll()

        // 일반회원 테이블 비우기
        normalUserJpaRepository.deleteAll()

        // 포지션 테이블 비우기
        positionsJpaRepository.deleteAll()
    }

    // 테스트 진행
    describe("일반회원에게 positionId에 해당하는 포지션을 등록할 때") {

        it("일반회원에 포지션이 정상적으로 등록돼야 한다.") {
            assertDoesNotThrow {
                sut.createByPositionId(userId = mockNormalUser.userId, positionId = mockPosition.positionId)
            }
        }
    }

    describe("일반회원이 회원 정보를 수정할 때") {

        it("일반회원에 포지션이 정상적으로 수정되야 한다.") {

        }
    }
})
