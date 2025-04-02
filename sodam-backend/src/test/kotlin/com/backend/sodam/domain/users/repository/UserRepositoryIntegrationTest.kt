package com.backend.sodam.domain.users.repository

import com.backend.sodam.domain.grades.entity.GradesEntity
import com.backend.sodam.domain.grades.repository.GradesJpaRepository
import com.backend.sodam.domain.grades.repository.UserGradeJpaRepository
import com.backend.sodam.domain.positions.entity.PositionsEntity
import com.backend.sodam.domain.positions.repository.PositionJpaRepository
import com.backend.sodam.domain.positions.repository.UserPositionJpaRepository
import com.backend.sodam.domain.subscriptions.entity.SubscriptionsEntity
import com.backend.sodam.domain.subscriptions.model.SubscriptionsType
import com.backend.sodam.domain.subscriptions.repository.SubscriptionJpaRepository
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionJpaRepository
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionRepository
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import io.kotest.extensions.spring.SpringExtension
import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import com.backend.sodam.domain.users.service.command.UserSignupCommand
import com.backend.sodam.domain.users.service.command.UserUpdateCommand
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.test.context.SpringBootTest
import java.util.*


/**
 * - 현재 UserRepository의 문제점은 SocialUser와 NormalUser가 엉켜있고 로직 자체도 처리가 이상함
 * - 구현체는 SocialUserRepository와 NormalUserRepository로 구분할 것이고
 * - 이를 FetchUserPort, CreateUserPort, UpdateUserPort, DeleteUserPort로 인터페이스를 추출할 것임
 * - 각 인터페이스에는 isTarget, isExistsByUserId와 같은 메서드가 반드시 포함되어야함
 */
@SpringBootTest // 현재 스프링 컨테이너 업로드해서 서비스에서 사용하는 빈들을 모두 관리해서 테스트 코드를 구동하고 있음 -> 이 부분 추후에 효율적으로 구성하기
class UserRepositoryIntegrationTest(
    // - 테스트 대상
    private val sut: NormalUserRepository,

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
): DescribeSpec({

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


    describe("회원 존재여부 확인 메서드 테스트") {
        context("이메일로 회원을 조회할 때") {
            // 일반 회원 엔티티 생성
            val userEntity = UsersEntity( userId = UUID.randomUUID().toString(),
                                          userEmail = "test@test.com",
                                          userName = "테스트 유저",
                                          introduce = "테스트 유저입니다.",
                                          profileImageUrl = "테스트 유저 프로필 사진",
                                          password = "asdf1234")


            it("해당 이메일과 관련된 회원이 존재한다면, true를 반환한다.") {
                // 일반 회원 엔티티 등록
                assertDoesNotThrow { userJpaRepository.save(userEntity) }

                val actual = sut.isExistsByEmail("test@test.com")
                actual shouldBe true
            }

            it("해당 이메일과 관련된 회원이 존재하지 않는다면, false를 반환한다.") {
                // 일반 회원 엔티티 등록
                assertDoesNotThrow { userJpaRepository.save(userEntity) }

                val actual = sut.isExistsByEmail("noexists@test.com")
                actual shouldBe false
            }
        }




        context("일반회원 등록할 때") {
            val command = UserSignupCommand(  email = "test@test.com",
                                              name = "테스트 유저",
                                              password = "asdf1234",
                                              positionId = mockPosition.positionId,
                                              profileImage = "테스트 유저 프로필 사진 url",
                                              introduce = "테스트 유저 입니다.")

            it("일반회원을 성공적으로 등록한다") {
                val actual = sut.createUser(command)
                val expected = SodamUser(
                    username = command.name,
                    email = command.email,
                    encryptedPassword = command.password,
                    profileImageUrl = command.profileImage,
                    introduce = command.introduce,
                )

                actual.email shouldBe expected.email
                actual.username shouldBe expected.username
                actual.introduce shouldBe command.introduce
                actual.profileImageUrl shouldBe command.profileImage
                actual.encryptedPassword shouldBe expected.encryptedPassword
            }

        }



        context("일반회원 업데이트할 때") {
            val command = UserSignupCommand(
                email = "test@test.com",
                name = "테스트 유저",
                password = "asdf1234",
                positionId = mockPosition.positionId,
                profileImage = "테스트 유저 프로필 사진 url",
                introduce = "테스트 유저 입니다.")

            it("업데이트 데이터와 userId를 제대로 전달했으면, 정상적으로 업데이트가 되야한다.") {
                // 변경할 대상 등록
                val target = sut.createUser(command)

                // 업데이트용 데이터, userId 사용
                val userId = target.userId
                val updateCommand = UserUpdateCommand(
                    email = "update@test.com",
                    name = "홍길동",
                    encryptedPassword = "asdf1234",
                    positionId = mockPosition.positionId,
                    introduce = "업데이트했습니다."
                )

                // 업데이트 처리
                val actual = sut.updateUserInfo(  userId = userId, userUpdateCommand = updateCommand)
                val expected = SodamUser(
                    userId = userId,
                    email = updateCommand.email,
                    username = updateCommand.name,
                    introduce = updateCommand.introduce,
                    encryptedPassword = updateCommand.encryptedPassword,
                )

                // 내용 비교
                actual.userId shouldBe expected.userId
                actual.email shouldBe expected.email
                actual.username shouldBe expected.username
                actual.introduce shouldBe expected.introduce
                actual.encryptedPassword shouldBe expected.encryptedPassword
            }
        }




        context("회원 유저아이디(userId, socialUserId)로 조회할 때") {
            val command = UserSignupCommand(
                email = "test@test.com",
                name = "테스트 유저",
                password = "asdf1234",
                positionId = mockPosition.positionId,
                profileImage = "테스트 유저 프로필 사진 url",
                introduce = "테스트 유저 입니다.")

            it("일반 회원 중에 존재하는 userId로 조회하면 정상적으로 조회가 되야한다.") { // 해당 부분은 구독권도 같이 등록해야함
                val target = sut.createUser(command)
                userSubscriptionRepository.createSubscriptionForUser(target.userId)

                val optional = sut.findByUserId(userId = target.userId)
                optional.isPresent shouldBe true

                val actual = optional.get()
                val expected = SodamUser(
                    userId = target.userId,
                    username = command.name,
                    encryptedPassword = command.password,
                    email = command.email,
                    introduce = command.introduce,
                    role = "ROLE_${SubscriptionsType.FREE.name}",
                    profileImageUrl = command.profileImage,
                    userType = UserType.NORMAL,
                )

                actual.userId shouldBe expected.userId
                actual.email shouldBe expected.email
                actual.username shouldBe expected.username
                actual.introduce shouldBe expected.introduce
                actual.encryptedPassword shouldBe expected.encryptedPassword
                actual.role shouldBe expected.role
                actual.profileImageUrl shouldBe expected.profileImageUrl
                actual.userType shouldBe expected.userType
            }
        }

        context("회원 프로필 정보 조회할 때") {

        }

        context("회원 자신이 작성한 게시글 조회할 때") {

        }

        context("회원 좋아요 게시글 조회할 때") {

        }
    }

})
