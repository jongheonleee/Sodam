import com.backend.sodam.SodamApplication
import com.backend.sodam.domain.articles.entity.ArticleEntity
import com.backend.sodam.domain.articles.repository.ArticleJpaRepository
import com.backend.sodam.domain.articles.service.response.ArticleSummaryResponse
import com.backend.sodam.domain.categories.entity.CategoryEntity
import com.backend.sodam.domain.categories.repository.CategoryJpaRepository
import com.backend.sodam.domain.grades.entity.GradesEntity
import com.backend.sodam.domain.grades.repository.GradesJpaRepository
import com.backend.sodam.domain.grades.repository.UserGradeJpaRepository
import com.backend.sodam.domain.positions.entity.PositionsEntity
import com.backend.sodam.domain.positions.exception.PositionException
import com.backend.sodam.domain.positions.repository.PositionJpaRepository
import com.backend.sodam.domain.positions.repository.UsersPositionJpaRepository
import com.backend.sodam.domain.subscriptions.entity.SubscriptionsEntity
import com.backend.sodam.domain.subscriptions.repository.SubscriptionJpaRepository
import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionJpaRepository
import com.backend.sodam.domain.tokens.repository.TokenJpaRepository
import com.backend.sodam.domain.users.entity.UsersEntity
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.service.UserService
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import com.backend.sodam.domain.users.service.command.UserSignupCommand
import com.backend.sodam.domain.users.service.command.UserUpdateCommand
import com.backend.sodam.domain.users.service.response.UserProfileResponse
import com.backend.sodam.domain.users.service.response.UserResponse
import com.backend.sodam.domain.users.service.response.UserSignupResponse
import com.backend.sodam.domain.users.service.response.UserUpdateResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.*

/**
 * 통합 테스트를 작성한 이유
 * - 현재 비즈니스 로직이 완전히 정의된게 아님
 * - 하지만, Mockito 를 활용한 단위 테스트를 작성할 경우 내부의 비즈니스 로직이 해당 테스트 코드에 노출됨
 * - 그래서 비즈니스 로직이 변경될 때 마다 테스트 코드에도 영향이 미침
 * - 따라서, 개발 초기에는 비즈니스 로직의 변동이 자주 일어나기 때문에 내부 비즈니스 로직을 노출시키지 않는 통합 테스트로 테스트 코드 작성
 */
@SpringBootTest(classes = [SodamApplication::class])
class UserServiceIntegrationTest(
    // 테스트 대상
    private val sut: UserService,

    // 테스트 환경 구축에 필요한 오브젝트
    // - 1. 기본적으로 세팅되어야 하는 데이터
    private val gradesJpaRepository: GradesJpaRepository,
    private val positionsJpaRepository: PositionJpaRepository,
    private val subscriptionJpaRepository: SubscriptionJpaRepository,

    // - 2. 회원과 연관된 교차 테이블
    private val userGradeJpaRepository: UserGradeJpaRepository,
    private val userPositionsJpaRepository: UsersPositionJpaRepository,
    private val userSubscriptionJpaRepository: UserSubscriptionJpaRepository,
    private val tokenJpaRepository: TokenJpaRepository,

    // - 3. 회원 테이블
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,

    // - 4. 그외 오브젝트
    private val articleJpaRepository: ArticleJpaRepository,
    private val categoryJpaRepository: CategoryJpaRepository
) : BehaviorSpec({

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

    val mockNormalUser = UsersEntity(
        userId = UUID.randomUUID().toString(),
        userName = "테스트 유저",
        userEmail = "duplicated@email.com",
        introduce = "테스트 유저",
        password = "password",
        profileImageUrl = "dedede"
    )

    val mockCategory = CategoryEntity(
        categoryId = UUID.randomUUID().toString(),
        topCategoryId = UUID.randomUUID().toString(),
        categoryName = "테스트용 카테고리",
        categoryOrd = 1,
        validYN = 0
    )

    // kotest에서 트랜잭션을 적용하려면 SpringExtension을 사용해야함
    extension(SpringExtension)

    // 서비스 통합 테스트 환경 구축
    beforeTest {
        // 모든 테이블 비우기
        // 회원과 관련된 테이블
        userPositionsJpaRepository.deleteAll()
        userGradeJpaRepository.deleteAll()
        userSubscriptionJpaRepository.deleteAll()
        tokenJpaRepository.deleteAll()

        // 회원 테이블 - 1. 일반 회원, 2. 소셜 회원
        normalUserJpaRepository.deleteAll()
        socialUserJpaRepository.deleteAll()

        // 회원 등록시 참고하는 테이블
        positionsJpaRepository.deleteAll()
        gradesJpaRepository.deleteAll()
        subscriptionJpaRepository.deleteAll()
        articleJpaRepository.deleteAll()
        categoryJpaRepository.deleteAll()

        // 회원가입 처리 등에서 요구되는 기본 데이터 셋 추가 - 1. 포지션, 2. 구독권, 3. 등급
        positionsJpaRepository.save(mockPosition)
        gradesJpaRepository.save(mockGrade)
        subscriptionJpaRepository.save(mockSubscription)
        normalUserJpaRepository.save(mockNormalUser)
        categoryJpaRepository.save(mockCategory)
    }

    // 테스트 환경 정리
    afterTest {
        // 회원 교차 테이블 비우기
        userPositionsJpaRepository.deleteAll()
        userGradeJpaRepository.deleteAll()
        userSubscriptionJpaRepository.deleteAll()
        tokenJpaRepository.deleteAll()


        // 회원과 연관되는 테이블 비우기
        positionsJpaRepository.deleteAll()
        gradesJpaRepository.deleteAll()
        subscriptionJpaRepository.deleteAll()

        // 일반회원 및 소셜회원 테이블 비우기
        normalUserJpaRepository.deleteAll()
        socialUserJpaRepository.deleteAll()

        // 게시글 테이블 지우기
        articleJpaRepository.deleteAll()
        categoryJpaRepository.deleteAll()

        // 테이블 초기화 되었는지 확인
        normalUserJpaRepository.count().shouldBe(0)
        socialUserJpaRepository.count().shouldBe(0)
        userPositionsJpaRepository.count().shouldBe(0)
        userGradeJpaRepository.count().shouldBe(0)
        userSubscriptionJpaRepository.count().shouldBe(0)
        positionsJpaRepository.count().shouldBe(0)
        gradesJpaRepository.count().shouldBe(0)
        subscriptionJpaRepository.count().shouldBe(0)
        articleJpaRepository.count().shouldBe(0)
        categoryJpaRepository.count().shouldBe(0)
    }

    /**
     * UserService 지원 기능
     * - 1. 일반회원 회원가입 => 성공/실패(중복된 이메일, 존재하지 않는 positionId)
     * - 2. 소셜회원 회원가입 => 성공
     * - 3. 회원정보 업데이트 => 성공/실패(중복된 이메일, 존재하지 않는 userId, 존재하지 않는 positionId)
     * - 4. 회원 이메일 조회 => 성공/실패(존재하지 않는 이메일)
     * - 5. 회원 아이디 조회 => 성공/실패(존재하지 않는 userId)
     * - 6. 카카오 회원 조회 => 보류
     * - 7. providerId로 조회 => 성공/실패(존재하지 않는 providerId -> null)
     * - 8. 회원 상세(프로필) 조회 => 설공/실패(존재하지 않는 userId)
     * - 9. 자신이 작성한 게시글 조회 => 성공
     * - 10. 자신의 좋아요 게시글 조회  => 성공
     */

    // 1. 일반 회원 회원가입 처리 테스트
    given("일반회원 회원가입을 할 때") {

        `when`("사용자가 유효한 데이터를 전달하면", {
            val command = UserSignupCommand(
                email = "test@test.com",
                name = "테스트 유저",
                password = "asdf1234",
                positionId = mockPosition.positionId,
                profileImage = "테스트 유저 프로필 사진 url",
                introduce = "테스트 유저 입니다."
            )

            val actual = sut.registerNormalUser(userSignupCommand = command)

            val expected = UserSignupResponse(
                username = command.name,
                email = command.email,
                encryptedPassword = command.password,
                introduce = command.introduce
            )
            then("성공적으로 회원가입이 되고 등록된 회원 정보의 일부를 반환한다.") {
                actual.email shouldBe expected.email
                actual.username shouldBe expected.username
                actual.introduce shouldBe command.introduce
                actual.encryptedPassword shouldBe expected.encryptedPassword
            }
        })

        `when`("사용자가 중복된 이메일을 전달하면", {
            // 중복된 이메일이 담긴 command
            val commandWithDuplicatedEmail = UserSignupCommand(
                email = mockNormalUser.userEmail,
                name = "테스트 유저",
                password = "asdf1234",
                positionId = mockPosition.positionId,
                profileImage = "테스트 유저 프로필 사진 url",
                introduce = "테스트 유저 입니다."
            )
            then("UserAlreadyExistsException 예외가 발생한다.") {
                assertThrows<UserException.UserAlreadyExistsException> {
                    sut.registerNormalUser(userSignupCommand = commandWithDuplicatedEmail)
                }
            }
        })

        `when`("사용자가 존재하지 않는 positionId를 전달하면", {
            val commandWithNotExistsPositionId = UserSignupCommand(
                email = "test@test.com",
                name = "테스트 유저",
                password = "asdf1234",
                positionId = "존재하지 않는 positionId",
                profileImage = "테스트 유저 프로필 사진 url",
                introduce = "테스트 유저 입니다."
            )
            then("PositionNotFoundException 예외가 발생한다.") {
                assertThrows<PositionException.PositionNotFoundException> {
                    sut.registerNormalUser(userSignupCommand = commandWithNotExistsPositionId)
                }
            }
        })
    }

    given("소셜회원 회원가입을 할 때") {

        `when`("사용자가 유효한 데이터를 전달하면", {
            val command = SocialUserSignupCommand(
                username = "테스트 유저",
                provider = "kakao",
                providerId = UUID.randomUUID().toString()
            )

            val actual = sut.registerSocialUser(socialUserSignupCommand = command)
            val expected = UserSignupResponse(
                username = command.username,
                encryptedPassword = "",
                email = "",
                introduce = ""
            )

            then("성공적으로 회원가입이 되고 등록된 회원 정보의 일부를 반환한다.") {
                actual.email shouldBe expected.email
                actual.username shouldBe expected.username
                actual.introduce shouldBe expected.introduce
                actual.encryptedPassword shouldBe expected.encryptedPassword
            }
        })
    }

    given("회원정보를 업데이트할 때") {

        // 1. 일반 회원
        `when`("사용자가 유효한 데이터를 전달하면", {
            // 업데이트 처리용 데이터
            val command = UserSignupCommand(
                email = "test@test.com",
                name = "테스트 유저",
                password = "asdf1234",
                positionId = mockPosition.positionId,
                profileImage = "테스트 유저 프로필 사진 url",
                introduce = "테스트 유저 입니다."
            )

            // 회원가입 처리
            sut.registerNormalUser(userSignupCommand = command)
            val target = sut.findByEmail(command.email)

            // 업데이트 처리용 데이터
            val updateCommand = UserUpdateCommand(
                email = "test2@gmail.com",
                name = "새로운 이름",
                encryptedPassword = "asdf1234",
                positionId = mockPosition.positionId,
                introduce = "업데이트용 테스트 유저"
            )

            // 업데이트 처리
            val actual = sut.updateUserInfo(userId = target.userId, userUpdateCommand = updateCommand)
            val expected = UserUpdateResponse(
                email = updateCommand.email,
                username = updateCommand.name,
                introduce = updateCommand.introduce,
                encryptedPassword = updateCommand.encryptedPassword
            )

            then("성공적으로 회원정보를 업데이트하고 변경된 회원 정보의 일부를 반환한다.") {
                actual.email shouldBe expected.email
                actual.username shouldBe expected.username
                actual.introduce shouldBe expected.introduce
                actual.encryptedPassword shouldBe expected.encryptedPassword
            }
        })

        `when`("사용자가 중복된 이메일을 전달하면", {
            // 업데이트 처리용 데이터
            val command = UserSignupCommand(
                email = "test@test.com",
                name = "테스트 유저",
                password = "asdf1234",
                positionId = mockPosition.positionId,
                profileImage = "테스트 유저 프로필 사진 url",
                introduce = "테스트 유저 입니다."
            )

            // 회원가입 처리
            sut.registerNormalUser(userSignupCommand = command)
            val target = sut.findByEmail(command.email)

            // 업데이트 처리용 데이터
            val updateCommandWithDuplicatedEmail = UserUpdateCommand(
                email = mockNormalUser.userEmail,
                name = "새로운 이름",
                encryptedPassword = "asdf1234",
                positionId = mockPosition.positionId,
                introduce = "업데이트용 테스트 유저"
            )

            then("UserAlreadyException 예외가 발생한다.") {
                assertThrows<UserException.UserAlreadyExistsException> {
                    sut.updateUserInfo(userId = target.userId, userUpdateCommand = updateCommandWithDuplicatedEmail)
                }
            }
        })

        `when`("사용자가 존재하지 않는 userId를 전달하면", {
            // 업데이트 처리용 데이터
            val command = UserSignupCommand(
                email = "test@test.com",
                name = "테스트 유저",
                password = "asdf1234",
                positionId = mockPosition.positionId,
                profileImage = "테스트 유저 프로필 사진 url",
                introduce = "테스트 유저 입니다."
            )

            // 회원가입 처리
            sut.registerNormalUser(userSignupCommand = command)
            val target = sut.findByEmail(command.email)

            // 업데이트 처리용 데이터
            val updateCommand = UserUpdateCommand(
                email = "degorso@gmail.com",
                name = "새로운 이름",
                encryptedPassword = "asdf1234",
                positionId = mockPosition.positionId,
                introduce = "업데이트용 테스트 유저"
            )
            val notExistsUserId = "npodawnpdwandaw"

            then("UserNotFoundException 예외가 발생한다.") {
                assertThrows<UserException.UserNotFoundException> {
                    sut.updateUserInfo(userId = notExistsUserId, userUpdateCommand = updateCommand)
                }
            }
        })

        `when`("사용자가 존재하지 않는 positionId를 전달하면", {
            // 업데이트 처리용 데이터
            val command = UserSignupCommand(
                email = "test@test.com",
                name = "테스트 유저",
                password = "asdf1234",
                positionId = mockPosition.positionId,
                profileImage = "테스트 유저 프로필 사진 url",
                introduce = "테스트 유저 입니다."
            )

            // 회원가입 처리
            sut.registerNormalUser(userSignupCommand = command)
            val target = sut.findByEmail(command.email)

            // 업데이트 처리용 데이터
            val updateCommandWithNotExistsPositionId = UserUpdateCommand(
                email = "degorso@gmail.com",
                name = "새로운 이름",
                encryptedPassword = "asdf1234",
                positionId = "Deafefeagr",
                introduce = "업데이트용 테스트 유저"
            )

            assertThrows<PositionException.PositionNotFoundException> {
                sut.updateUserInfo(userId = target.userId, userUpdateCommand = updateCommandWithNotExistsPositionId)
            }
        })

        // 2. 소셜 회원
        `when`("사용자가 유효한 데이터를 전달하면", {
            // 소셜회원 등록
            val command = SocialUserSignupCommand(
                username = "테스트 유저",
                provider = "kakao",
                providerId = UUID.randomUUID().toString()
            )

            // 회원가입 처리
            sut.registerSocialUser(socialUserSignupCommand = command)
            val target = sut.findByProviderId(command.providerId)

            val updateCommand = UserUpdateCommand(
                email = "test2@gmail.com",
                name = "새로운 이름",
                encryptedPassword = "asdf1234",
                positionId = mockPosition.positionId,
                introduce = "업데이트용 테스트 유저"
            )

            val actual = sut.updateUserInfo(userId = target!!.userId, userUpdateCommand = updateCommand)
            val expected = UserUpdateResponse(
                username = updateCommand.name,
                email = updateCommand.email,
                introduce = updateCommand.introduce
            )

            then("성공적으로 회원정보를 업데이트하고 변경된 회원 정보의 일부를 반환한다.") {
                actual.email shouldBe expected.email
                actual.username shouldBe expected.username
                actual.introduce shouldBe expected.introduce
            }
        })

        `when`("사용자가 중복된 이메일을 전달하면", {
            // 소셜회원 등록
            val command = SocialUserSignupCommand(
                username = "테스트 유저",
                provider = "kakao",
                providerId = UUID.randomUUID().toString()
            )

            // 회원가입 처리
            sut.registerSocialUser(socialUserSignupCommand = command)
            val target = sut.findByProviderId(command.providerId)

            val updateCommand = UserUpdateCommand(
                email = mockNormalUser.userEmail,
                name = "새로운 이름",
                encryptedPassword = "asdf1234",
                positionId = mockPosition.positionId,
                introduce = "업데이트용 테스트 유저"
            )

            then("UserAlreadyException 예외가 발생한다.") {
                assertThrows<UserException.UserAlreadyExistsException> {
                    sut.updateUserInfo(userId = target!!.userId, userUpdateCommand = updateCommand)
                }
            }
        })

        `when`("사용자가 존재하지 않는 userId를 전달하면", {
            // 소셜회원 등록
            val command = SocialUserSignupCommand(
                username = "테스트 유저",
                provider = "kakao",
                providerId = UUID.randomUUID().toString()
            )

            // 회원가입 처리
            sut.registerSocialUser(socialUserSignupCommand = command)
            val target = sut.findByProviderId(command.providerId)

            val updateCommand = UserUpdateCommand(
                email = "test2@gmail.com",
                name = "새로운 이름",
                encryptedPassword = "asdf1234",
                positionId = mockPosition.positionId,
                introduce = "업데이트용 테스트 유저"
            )
            val notExistsUserId = "npodawnpdwandaw"

            then("UserNotFoundException 예외가 발생한다.") {
                assertThrows<UserException.UserNotFoundException> {
                    sut.updateUserInfo(userId = notExistsUserId, userUpdateCommand = updateCommand)
                }
            }
        })

        `when`("사용자가 존재하지 않는 positionId를 전달하면", {
            // 소셜회원 등록
            val command = SocialUserSignupCommand(
                username = "테스트 유저",
                provider = "kakao",
                providerId = UUID.randomUUID().toString()
            )

            // 회원가입 처리
            sut.registerSocialUser(socialUserSignupCommand = command)
            val target = sut.findByProviderId(command.providerId)

            val updateCommandWithNotExistsPositionId = UserUpdateCommand(
                email = "test2@gmail.com",
                name = "새로운 이름",
                encryptedPassword = "asdf1234",
                positionId = "dwadwagr",
                introduce = "업데이트용 테스트 유저"
            )

            assertThrows<PositionException.PositionNotFoundException> {
                sut.updateUserInfo(userId = target!!.userId, userUpdateCommand = updateCommandWithNotExistsPositionId)
            }
        })
    }

    given("회원 이메일 조회할 때") {

        `when`("존재하는 이메일을 전달하면", {
            // 업데이트 처리용 데이터
            val command = UserSignupCommand(
                email = "test@test.com",
                name = "테스트 유저",
                password = "asdf1234",
                positionId = mockPosition.positionId,
                profileImage = "테스트 유저 프로필 사진 url",
                introduce = "테스트 유저 입니다."
            )

            // 회원가입 처리
            sut.registerNormalUser(userSignupCommand = command)

            // 이메일 조회
            val actual = sut.findByEmail(email = command.email)
            val expected = UserResponse(
                userId = "dwede",
                email = command.email,
                name = command.name,
                password = command.password,
                profileImage = command.profileImage,
                introduce = command.introduce,
                role = "ROLE_FREE"
            )

            then("등록된 회원 정보를 반환한다.") {
                actual.email shouldBe expected.email
                actual.name shouldBe expected.name
                actual.password shouldBe expected.password
                actual.profileImage shouldBe expected.profileImage
                actual.introduce shouldBe expected.introduce
                actual.role shouldBe expected.role
            }
        })

        `when`("존재하지 않는 이메일을 전달하면", {
            // 업데이트 처리용 데이터
            val command = UserSignupCommand(
                email = "test@test.com",
                name = "테스트 유저",
                password = "asdf1234",
                positionId = mockPosition.positionId,
                profileImage = "테스트 유저 프로필 사진 url",
                introduce = "테스트 유저 입니다."
            )

            // 회원가입 처리
            sut.registerNormalUser(userSignupCommand = command)

            // 이메일 조회
            then("UserNotFoundException 예외가 발생한다.") {
                assertThrows<UserException.UserNotFoundException> {
                    sut.findByEmail(email = "Dedededede")
                }
            }
        })
    }

    // 이 부분은 추후에 단위 테스트에서 진행
    // - userId는 uuid이기 때문임
    given("회원 아이디 조회할 때") {

        `when`("존재하는 아이디를 전달하면", {

            then("등록된 회원 정보를 반환한다.") {
            }
        })

        `when`("존재하지 않는 아이디를 전달하면", {

            then("UserNotFoundException 예외가 발생한다.") {
            }
        })
    }

    given("키키오 회원 조회를 할 때") {

        `when`("존재하는 토큰이 전달하면", {

            then("카카오 회원을 반환한다.") {
            }
        })
    }

    given("providerId로 회원을 조회할 때") {

        `when`("providerId가 존재하면", {
            val command = SocialUserSignupCommand(
                username = "테스트 유저",
                provider = "kakao",
                providerId = UUID.randomUUID().toString()
            )

            sut.registerSocialUser(socialUserSignupCommand = command)

            val actual = sut.findByProviderId(providerId = command.providerId)!!
            val expected = UserResponse(
                userId = "",
                email = "",
                name = command.username,
                password = "",
                profileImage = "",
                introduce = "",
                role = "ROLE_FREE"
            )

            then("등록되어 있는 소셜회원 정보를 반환한다.") {
                actual.email shouldBe expected.email
                actual.name shouldBe expected.name
                actual.introduce shouldBe expected.introduce
                actual.role shouldBe expected.role
            }
        })

        `when`("providerId가 존재하지 않는다면", {
            val command = SocialUserSignupCommand(
                username = "테스트 유저",
                provider = "kakao",
                providerId = UUID.randomUUID().toString()
            )

            sut.registerSocialUser(socialUserSignupCommand = command)
            val notExistsProviderId = "dwadwadwa"
            val actual = sut.findByProviderId(providerId = notExistsProviderId)

            then("Null이 반환된다.") {
                actual shouldBe null
            }
        })
    }

    given("회원 상세(프로필) 조회할 때") {

        `when`("존재하는 userId를 전달하면", {

            val command = UserSignupCommand(
                email = "test@test.com",
                name = "테스트 유저",
                password = "asdf1234",
                positionId = mockPosition.positionId,
                profileImage = "테스트 유저 프로필 사진 url",
                introduce = "테스트 유저 입니다."
            )

            val result = sut.registerNormalUser(userSignupCommand = command)
            val target = sut.findByEmail(result.email)
            val actual = sut.findUserProfileInfo(userId = target.userId)

            val expected = UserProfileResponse(
                userId = target.userId,
                name = command.name,
                email = command.email,
                introduce = command.introduce,
                profileImageUrl = "",
                subscription = "FREE",
                articleTotalCnt = 0,
                grade = "ENTRY",
                ranking = 5000,
                positions = listOf("미정")
            )

            then("해당 유저의 상세 정보(프로필)을 반환한다.") {
                actual.userId shouldBe expected.userId
                actual.email shouldBe expected.email
                actual.name shouldBe expected.name
                actual.introduce shouldBe expected.introduce
                actual.grade shouldBe expected.grade
                actual.articleTotalCnt shouldBe expected.articleTotalCnt
                actual.ranking shouldBe expected.ranking
                actual.positions[0] shouldBe expected.positions[0]
            }
        })

        `when`("존재하지 않는 userId를 전달하면", {
            val command = UserSignupCommand(
                email = "test@test.com",
                name = "테스트 유저",
                password = "asdf1234",
                positionId = mockPosition.positionId,
                profileImage = "테스트 유저 프로필 사진 url",
                introduce = "테스트 유저 입니다."
            )

            val result = sut.registerNormalUser(userSignupCommand = command)
            val notExistsUserId = "dwadwadw"

            then("UserNotFoundException 예외가 발생한다.") {
                assertThrows<UserException.UserNotFoundException> {
                    sut.findUserProfileInfo(userId = notExistsUserId)
                }
            }
        })
    }

    given("회원이 작성한 게시글 조회할 때") {
        // 1. 일반회원

        `when`("존재하는 회원이면") {
            // 목 회원, 목 카테고리 활용

            // 게시글 여러개 등록
            for (i in 1..5) {
                val mockArticle = ArticleEntity(
                    name = mockNormalUser.userName,
                    user = mockNormalUser,
                    articleTitle = "테스트 제목$i",
                    articleSummary = "테스트 서머리$i",
                    articleContent = "테스트 내용$i",
                    articleViewCnt = 0,
                    articleLikeCnt = 0,
                    articleDislikeCnt = 0,
                    category = mockCategory
                )
                articleJpaRepository.save(mockArticle)
            }

            // 조회 처리
            val pageable = PageRequest.of(0, 20)
            val actual = sut.getOwnArticles(pageable = pageable, userId = mockNormalUser.userId)
            val expected = PageImpl(
                listOf(
                    ArticleSummaryResponse(
                        articleId = 0,
                        username = mockNormalUser.userName,
                        profileImageUrl = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        title = "테스트 제목1",
                        summary = "테스트 서머리1",
                        createdAt = LocalDateTime.now().toString(),
                        tags = listOf()
                    ),
                    ArticleSummaryResponse(
                        articleId = 0,
                        username = mockNormalUser.userName,
                        profileImageUrl = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        title = "테스트 제목2",
                        summary = "테스트 서머리2",
                        createdAt = LocalDateTime.now().toString(),
                        tags = listOf()
                    ),
                    ArticleSummaryResponse(
                        articleId = 0,
                        username = mockNormalUser.userName,
                        profileImageUrl = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        title = "테스트 제목3",
                        summary = "테스트 서머리3",
                        createdAt = LocalDateTime.now().toString(),
                        tags = listOf()
                    ),
                    ArticleSummaryResponse(
                        articleId = 0,
                        username = mockNormalUser.userName,
                        profileImageUrl = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        title = "테스트 제목4",
                        summary = "테스트 서머리4",
                        createdAt = LocalDateTime.now().toString(),
                        tags = listOf()
                    ),
                    ArticleSummaryResponse(
                        articleId = 0,
                        username = mockNormalUser.userName,
                        profileImageUrl = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        title = "테스트 제목5",
                        summary = "테스트 서머리5",
                        createdAt = LocalDateTime.now().toString(),
                        tags = listOf()
                    )
                ),
                pageable,
                5
            )

            // 내용 비교 데이터 생성
            then("자신이 작성한 게시글을 성공적으로 반환한다.") {
                // 내용 비교
                actual.totalPages shouldBe expected.totalPages
                val actualResults = actual.get().toList()
                val expectedResults = expected.toList()

                for (i in 0..4) {
                    val a = actualResults[i]
                    val b = expectedResults[i]

                    a.title shouldBe a.title
                    a.summary shouldBe a.summary
                    a.username shouldBe a.username
                }
            }
        }

        `when`("존재하지 않는 회원이라면") {
            for (i in 1..5) {
                val mockArticle = ArticleEntity(
                    name = mockNormalUser.userName,
                    user = mockNormalUser,
                    articleTitle = "테스트 제목$i",
                    articleSummary = "테스트 서머리$i",
                    articleContent = "테스트 내용$i",
                    articleViewCnt = 0,
                    articleLikeCnt = 0,
                    articleDislikeCnt = 0,
                    category = mockCategory
                )
                articleJpaRepository.save(mockArticle)
            }

            // 조회 처리
            val pageable = PageRequest.of(0, 20)
            val notExistsUserId = "dwadwadaw"
            then("UserNotFoundException 예외가 발생한다.") {
                // 예외 발생 여부 확인
                assertThrows<UserException.UserNotFoundException> {
                    sut.getOwnArticles(pageable = pageable, userId = notExistsUserId)
                }
            }
        }
    }
})
