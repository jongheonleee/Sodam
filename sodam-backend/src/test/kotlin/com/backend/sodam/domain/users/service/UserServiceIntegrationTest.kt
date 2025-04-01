package com.backend.sodam.domain.users.service 
import com.backend.sodam.domain.grades.entity.GradesEntity
import com.backend.sodam.domain.grades.repository.GradesJpaRepository
import com.backend.sodam.domain.grades.repository.UserGradeJpaRepository
import com.backend.sodam.domain.positions.entity.PositionsEntity
import com.backend.sodam.domain.positions.repository.PositionJpaRepository
import com.backend.sodam.domain.positions.repository.UserPositionJpaRepository
import com.backend.sodam.domain.subscriptions.entity.SubscriptionsEntity
import com.backend.sodam.domain.subscriptions.repository.SubscriptionJpaRepository
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionJpaRepository
import com.backend.sodam.domain.users.controller.response.UserSignupResponse
import com.backend.sodam.domain.users.controller.response.UserUpdateResponse
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import com.backend.sodam.domain.users.service.command.UserSignupCommand
import com.backend.sodam.domain.users.service.command.UserUpdateCommand
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

/**
 * 통합 테스트를 작성한 이유
 * - 현재 비즈니스 로직이 완전히 정의된게 아님
 * - 하지만, Mockito 를 활용한 단위 테스트를 작성할 경우 내부의 비즈니스 로직이 해당 테스트 코드에 노출됨
 * - 그래서 비즈니스 로직이 변경될 때 마다 테스트 코드에도 영향이 미침
 * - 따라서, 개발 초기에는 비즈니스 로직의 변동이 자주 일어나기 때문에 내부 비즈니스 로직을 노출시키지 않는 통합 테스트로 테스트 코드 작성
 */
@SpringBootTest
class UserServiceIntegrationTest(
    // 테스트 대상
    private val sut: UserService,

    // 테스트 환경 구축에 필요한 오브젝트
    // - 1. 기본적으로 세팅되어야 하는 데이터
    private val positionsJpaRepository: PositionJpaRepository,
    private val gradesJpaRepository: GradesJpaRepository,
    private val subscriptionJpaRepository: SubscriptionJpaRepository,

    // - 2. 회원과 연관된 교차 테이블
    private val userPositionsJpaRepository: UserPositionJpaRepository,
    private val userGradeJpaRepository: UserGradeJpaRepository,
    private val userSubscriptionJpaRepository: UserSubscriptionJpaRepository,

    // - 3. 회원 테이블
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
): BehaviorSpec({

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

    // 서비스 통합 테스트 환경 구축
    beforeTest {
        // 모든 테이블 비우기
        // 회원과 관련된 테이블
        userPositionsJpaRepository.deleteAll()
        userGradeJpaRepository.deleteAll()
        userSubscriptionJpaRepository.deleteAll()

        // 회원 테이블 - 1. 일반 회원, 2. 소셜 회원
        normalUserJpaRepository.deleteAll()
        socialUserJpaRepository.deleteAll()

        // 회원 등록시 참고하는 테이블
        positionsJpaRepository.deleteAll()
        gradesJpaRepository.deleteAll()
        subscriptionJpaRepository.deleteAll()

        // 회원가입 처리 등에서 요구되는 기본 데이터 셋 추가 - 1. 포지션, 2. 구독권, 3. 등급
        positionsJpaRepository.save(mockPosition)
        gradesJpaRepository.save(mockGrade)
        subscriptionJpaRepository.save(mockSubscription)

    }


    // 1. 일반 회원 회원가입 처리 테스트
    given("(1) 사용자가 올바른 형태의 데이터를 전달했고") {
        val command = UserSignupCommand(
            email = "test@test.com",
            name = "테스트용 이름",
            password = "password123",
            positionId = mockPosition.positionId,
            profileImage = "이미지 url",
            introduce = "테스트용 더미 데이터"
        )

        `when`("회원가입 메서드를 호출하면", {
            val actual = sut.signupUser(command)
            val expected = UserSignupResponse(
                username = "테스트용 이름",
                encryptedPassword = "password123",
                email = "test@test.com",
                introduce = "테스트용 더미 데이터"
            )

            then("등록된 회원 정보를 성공적으로 반환한다.") {
                actual.email shouldBe expected.email
                actual.username shouldBe expected.username
                actual.introduce shouldBe expected.introduce
                actual.encryptedPassword shouldBe expected.encryptedPassword
            }
        })
    }

    given("(2) 사용자가 중복된 이메일을 전달했고") {
        val command = UserSignupCommand(
            email = "test@test.com",
            name = "테스트용 이름",
            password = "password123",
            positionId = mockPosition.positionId,
            profileImage = "이미지 url",
            introduce = "테스트용 더미 데이터"
        )

        `when`("회원가입 메서드를 호출하면", {
            // 회원 1개 사전에 등록
            sut.signupUser(command)

            val actual = shouldThrow<UserException.UserAlreadyExistsException> {
                sut.signupUser(command)
            }

            then("UserAlreadyExistsException 예외가 발생한다.") {
                actual shouldBe UserException.UserAlreadyExistsException()
            }
        })
    }

    // 2. 소셜회원 회원가입 처리 테스트
    given("(1) 사용자가 올바른 형태에 데이터를 전달했고") {
        val command = SocialUserSignupCommand(
            username = "테스트용 이름",
            provider = "kakao",
            providerId = "213957292"
        )

        `when`("소셜 회원가입 메서드를 호출하면", {
            val expected = UserSignupResponse( username = "테스트용 이름",
                                               encryptedPassword = "",
                                               email = "",
                                               introduce = "")
            val actual = sut.signupSocialUser(command)


            then("등록된 회원 정보를 성공적으로 반환한다.") {
                actual.username shouldBe expected.username
                actual.encryptedPassword shouldBe expected.encryptedPassword
                actual.email shouldBe expected.email
                actual.introduce shouldBe expected.introduce
            }
        })

    }

    given("(2) 사용자가 전달한 데이터에서 providerId가 이미 등록된 아이디일 때", {
        val command = SocialUserSignupCommand(
            username = "테스트용 이름",
            provider = "kakao",
            providerId = "213957292"
        )


        `when`("소셜 회원가입 메서드를 호출하면", {
            // 사전에 미리 등록
            sut.signupSocialUser(command)

            val actual = shouldThrow<UserException.SocialUserAlreadyExistsException> {
                sut.signupSocialUser(command)
            }
            then("SocialUserAlreadyExistsException 예외가 발생한다.") {
                actual shouldBe UserException.SocialUserAlreadyExistsException()
            }
        })
    })

    // 3. 회원 유저 정보 수정 처리 테스트
    // 3-1. 일반 회원의 경우
    given("[일반회원](1) 사용자가 올바른 데이터를 전달했고") {
        val command = UserSignupCommand(
            email = "test@test.com",
            name = "테스트용 이름",
            password = "password123",
            positionId = mockPosition.positionId,
            profileImage = "이미지 url",
            introduce = "테스트용 더미 데이터"
        )

        `when`("회원 수정 메서드를 호출하면", {
            // 회원 등록
            val response = sut.signupUser(command)
            val target = normalUserJpaRepository.findByUserEmail(response.email).get()

            // 회원 수정을 위한 작업 세팅
            val updateCommand = UserUpdateCommand(
                email = "new@test.com",
                name = "업데이트된 테스트용 이름",
                encryptedPassword = "password123",
                positionId = mockPosition.positionId,
                introduce = "업데이트된 테스트용 더미 데이터"
            )

            // 회원 정보 수정 처리
            val actual = sut.updateUserInfo(
                userId = target.userId,
                userUpdateCommand = updateCommand,
            )
            val expected = UserUpdateResponse(
                username = "업데이트된 테스트용 이름",
                encryptedPassword = "password123",
                email = "new@test.com",
                introduce = "업데이트된 테스트용 더미 데이터"
            )

            then("수정된 회원 정보가 성공적으로 반환된다.") {
                actual.email shouldBe expected.email
                actual.username shouldBe expected.username
                actual.introduce shouldBe expected.introduce
                actual.encryptedPassword shouldBe expected.encryptedPassword
            }
        })
    }

    given("[일반회원](2) 사용자가 전달한 데이터에서 userId에 해당하는 유저가 없는 경우") {
        val notExistsUserId = "dwalfnrg123"
        val updateCommand = UserUpdateCommand(
            email = "new@test.com",
            name = "업데이트된 테스트용 이름",
            encryptedPassword = "password123",
            positionId = mockPosition.positionId,
            introduce = "업데이트된 테스트용 더미 데이터"
        )

        `when`("회원 수정 메서드를 호출하면", {
            val actual = shouldThrow<UserException.UserNotFoundException> {
                sut.updateUserInfo(
                    userId = notExistsUserId,
                    userUpdateCommand = updateCommand,
                )
            }

            then("UserNotFoundException 예외가 발생한다.") {
                actual shouldBe UserException.UserNotFoundException()
            }
        })
    }

    given("[일반회원](3) 사용자가 중복된 이메일을 전달한 경우") {
        val commandFirstUser = UserSignupCommand(
            email = "test@test.com",
            name = "테스트용 이름",
            password = "password123",
            positionId = mockPosition.positionId,
            profileImage = "이미지 url",
            introduce = "테스트용 더미 데이터"
        )

        val commandSecondUser = UserSignupCommand(
            email = "duplicatedemail@test.com",
            name = "테스트용 이름",
            password = "password123",
            positionId = mockPosition.positionId,
            profileImage = "이미지 url",
            introduce = "테스트용 더미 데이터"
        )


        `when`("회원 수정 메서드를 호출하면", {
            // 회원 등록
            val response = sut.signupUser(commandFirstUser)
            sut.signupUser(commandSecondUser)
            val target = normalUserJpaRepository.findByUserEmail(response.email).get()

            // 회원 수정을 위한 작업 세팅
            val updateCommandWithDuplicatedEmail = UserUpdateCommand(
                email = "duplicatedemail@test.com",
                name = "업데이트된 테스트용 이름",
                encryptedPassword = "password123",
                positionId = mockPosition.positionId,
                introduce = "업데이트된 테스트용 더미 데이터"
            )

            val actual = shouldThrow<UserException.UserAlreadyExistsException> {
                sut.updateUserInfo(
                    userId = target.userId,
                    userUpdateCommand = updateCommandWithDuplicatedEmail,
                )
            }

            then("UserAlreadyExistsException 예외가 발생한다.") {
                actual shouldBe UserException.UserAlreadyExistsException()
            }
        })
    }

    // 3-2. 소셜 회원의 경우
    given("[소셜회원](1) 사용자가 올바른 데이터를 전달했고") {
        `when`("회원 수정 메서드를 호출하면", {
            // 회원 등록
            val command = SocialUserSignupCommand(
                username = "테스트용 이름",
                provider = "kakao",
                providerId = "213957292"
            )

            // 회원 수정을 위한 작업 세팅
            val updateCommand = UserUpdateCommand(
                email = "new@test.com",
                name = "업데이트된 테스트용 이름",
                encryptedPassword = "password123",
                positionId = mockPosition.positionId,
                introduce = "업데이트된 테스트용 더미 데이터"
            )

            // 회원 등록 및 해당 회원 조회
            sut.signupSocialUser(command)
            val target = socialUserJpaRepository.findByProviderId(providerId = command.providerId).get()
            socialUserJpaRepository.findAll().forEach {
                println("여기다")
                println("it's socialId : " + it.socialUserId)
                println("it's providerId : " + it.providerId)
                println("target's socialId : " + target.socialUserId)
                println("target's providerId : " + target.providerId)
                println("is exists? : " + socialUserJpaRepository.existsBySocialUserId(target.socialUserId))
            }

            // 기대한 결과
            val expected = UserUpdateResponse(
                email = "new@test.com",
                username = "업데이트된 테스트용 이름",
                encryptedPassword = "password123",
                introduce = "업데이트된 테스트용 더미 데이터"
            )

            then("수정된 회원 정보가 성공적으로 반환된다.") {
                val actual = sut.updateUserInfo(
                    userId = target.socialUserId,
                    userUpdateCommand = updateCommand,
                )

                actual.email shouldBe expected.email
                actual.username shouldBe expected.username
                actual.introduce shouldBe expected.introduce
                actual.encryptedPassword shouldBe expected.encryptedPassword
            }
        })
    }

    given("[소셜회원](2) 사용자가 전달한 데이터에서 userId에 해당하는 유저가 없는 경우") {
        `when`("회원 수정 메서드를 호출하면", {


            then("UserNotFoundException 예외가 발생한다.") {

            }
        })
    }

    given("[소셜회원](3) 사용자가 중복된 이메일을 전달한 경우") {
        `when`("회원 수정 메서드를 호출하면", {

            then("UserAlreadyExistsException 예외가 발생한다.") {

            }
        })
    }

    given("4. 회원 이메일로 조회") {
        // TODO: 테스트 코드 작성
    }

    given("5. 회원 아이디로 조회") {
        // TODO: 테스트 코드 작성
    }

    given("6. 회원 토큰으로 카카오 유저 조회") {
        // TODO: 테스트 코드 작성
    }

    given("7. 회원 providerId로 조회") {
        // TODO: 테스트 코드 작성
    }

    given("8. 회원 프로필 정보 조회") {
        // TODO: 테스트 코드 작성
    }

    given("9. 회원 자신의 게시글 조회") {
        // TODO: 테스트 코드 작성
    }

    given("10. 회원 자신이 좋아요 누른 게시글 조회") {
        // TODO: 테스트 코드 작성
    }
})
