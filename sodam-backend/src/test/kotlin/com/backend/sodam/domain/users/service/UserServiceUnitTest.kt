package com.backend.sodam.domain.users.service

import com.backend.sodam.domain.subscriptions.exception.SubscriptionException
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.service.command.SocialUserSignupCommand
import com.backend.sodam.domain.users.service.command.UserSignupCommand
import com.backend.sodam.domain.users.service.response.SimpleUserResponse
import com.backend.sodam.domain.users.service.response.SocialUserResponse
import com.backend.sodam.domain.users.service.response.UserResponse
import com.backend.sodam.domain.users.service.response.UserSignupResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows

class UserServiceUnitTest : BehaviorSpec({

    // 제공되는 기능
    // - 1. 회원가입
    // - 2. 소셜 유저 회원가입
    // - 3. 이메일로 단순 유저 조회
    // - 4. 소셜 유저 조회(카카오)
    // - 5. providerId로 회원 조회
    val sut = mockk<UserService>()

    // - 1. 회원가입 처리
    // - 회원가입 성공
    // - 중복된 이메일로 인한 예외 발생
    // - 회원 구독권 생성시 예외 발생
    given("클라이언트로부터 회원가입 요청을 받았을 때") {
        `when`("중복되는 이메일이 없다면") {
            // 전달 받은 데이터, 반환할 데이터 세팅
            val command = UserSignupCommand(
                email = "asdf1234@gmail.com",
                name = "테스트 유저",
                password = "asdf1234",
                profileImage = "aws에 등록된 프로필 이미지",
                introduce = "테스트 자기소개글"
            )

            val expected = UserSignupResponse(
                username = "테스트 유저",
                encryptedPassword = "asdf1234",
                email = "asdf1234@gmail.com",
                introduce = "테스트 자기소개글"
            )

            // 목킹
            every { sut.signupUser(command) } returns expected

            then("성공적으로 회원가입 응답 객체를 반환한다.") {
                // 실제 호출
                val actual = sut.signupUser(command)

                // 내용 비교
                actual shouldBe expected

                // 호출 검증
                verify {
                    sut.signupUser(command)
                }
            }
        }

        `when`("중복되는 이메일이 있다면") {
            // 전달 받은 데이터, 반환할 데이터 세팅
            val dto = UserSignupCommand(
                email = "asdf1234@gmail.com",
                name = "테스트 유저",
                password = "asdf1234",
                profileImage = "aws에 등록된 프로필 이미지",
                introduce = "테스트 자기소개글"
            )

            val expected = UserException.UserAlreadyExistsException()

            // 목킹
            every { sut.signupUser(dto) } throws expected

            then("UserAlreadyExistsException 예외가 발생해야 한다.") {
                assertThrows<UserException.UserAlreadyExistsException> {
                    sut.signupUser(dto)
                }
            }
        }

        `when`("회원 무료 구독권 발급과정에서 무료 구독권을 조회하지 못하면") {
            // 전달 받은 데이터, 반환할 데이터 세팅
            val dto = UserSignupCommand(
                email = "asdf1234@gmail.com",
                name = "테스트 유저",
                password = "asdf1234",
                profileImage = "aws에 등록된 프로필 이미지",
                introduce = "테스트 자기소개글"
            )

            val expected = SubscriptionException.SubscriptionNotFoundException()

            every { sut.signupUser(dto) } throws expected

            then("SubscriptionNotFoundException 예외가 발생해야 한다.") {
                assertThrows<SubscriptionException.SubscriptionNotFoundException> {
                    sut.signupUser(dto)
                }

                verify {
                    sut.signupUser(dto)
                }
            }
        }
    }

    // - 2. 소셜 유저 회원가입
    // - 소셜 회원가입 성공
    // - 이미 등록된 providerId라면 SocialUserAlreadyExistsException 예외가 발생
    given("클라이언트로부터 소셜 회원가입 요청을 받았을 때") {
        `when`("등록된 providerId가 존재하지 않는다면") {
            val request = SocialUserSignupCommand(
                username = "테스트 유저",
                provider = "테스트 프로바이더",
                providerId = "테스트 프로바이더 아이디"
            )

            val expected = UserSignupResponse(
                username = "테스트 유저"
            )

            every { sut.signupSocialUser(request) } returns expected

            then("소셜 회원가입에 성공한다.") {
                sut.signupSocialUser(request) shouldBe expected

                verify {
                    sut.signupSocialUser(request)
                }
            }
        }

        `when`("이미 등록된 providerId가 존재한다면") {
            val request = SocialUserSignupCommand(
                username = "테스트 유저",
                provider = "테스트 프로바이더",
                providerId = "테스트 프로바이더 아이디"
            )

            val expected = UserException.SocialUserAlreadyExistsException()

            every { sut.signupSocialUser(request) } throws expected

            then(" SocialUserAlreadyExistsException 예외가 발생한다.") {
                assertThrows<UserException.SocialUserAlreadyExistsException> {
                    sut.signupSocialUser(request)
                }

                verify {
                    sut.signupSocialUser(request)
                }
            }
        }
    }

    // - 3. 이메일로 단순 유저 조회
    // - 단순 유저 조회 성공
    // - 전달받은 이메일이 없는 경우 UserNotFoundException 예외 발생
    given("클라이언트로부터 이메일로 단순 유저 조회 요청을 받았을 때") {

        `when`("이메일에 대한 유저가 존재한다면") {
            val email = "asdf1234@gmail.com"
            val expected = SimpleUserResponse(
                username = "테스트 유저",
                email = "asdf1234@gmail.com"
            )

            every { sut.findByUserEmail(email) }.returns(expected)

            then("단순 유저 정보를 반환한다.") {
                val actual = sut.findByUserEmail(email)

                actual shouldBe expected

                verify {
                    sut.findByUserEmail(email)
                }
            }
        }

        `when`("이메일에 대한 유저가 존재하지 않는다면") {
            val email = "asdf1234@gmail.com"
            val expected = UserException.UserNotFoundException()

            every { sut.findByUserEmail(email) }.throws(expected)

            then("UserNotFoundException 예외가 발생한다.") {
                assertThrows<UserException.UserNotFoundException> {
                    sut.findByUserEmail(email)
                }

                verify {
                    sut.findByUserEmail(email)
                }
            }
        }
    }

    // - 4. 소셜 유저 조회(카카오)
    // - 소셜 유저 조회 성공
    given("클라이언트로부터 accessToken으로 소셜 유저 조회 요청을 받았을 때") {

        `when`("accessToken으로 부터 SNS 유저를 조회할 수 있다면") {
            val accessToken = "테스트 토큰"
            val expected = SocialUserResponse(
                name = "테스트 유저",
                provider = "테스트 프로바이더",
                providerId = "테스트 프로바이더 아이디"
            )

            every { sut.findKakaoUser(accessToken) }.returns(expected)

            then("소셜 유저 정보를 반환한다.") {
                val actual = sut.findKakaoUser(accessToken)

                actual shouldBe expected

                verify {
                    sut.findKakaoUser(accessToken)
                }
            }
        }
    }

    // - 5. providerId로 회원 조회
    // - 회원 조회 성공
    given("클라이언트로부터 providerId로 회원 정보 조회 요청을 받았을 때") {

        `when`("providerId로 등록된 회원이 있다면") {
            val providerId = "테스트 프로바이더 아이디"
            val expected = UserResponse(
                userId = "테스트 유저 아이디",
                email = "asdf1234@gmail.com",
                name = "테스트 유저",
                password = "테스트 유저 비밀번호",
                profileImage = "테스트 유저 프로필 이미지 url",
                introduce = "테스트 유저 자기 소개글"
            )

            every { sut.findByProviderId(providerId) }.returns(expected)

            then("회원 정보를 반환한다.") {
                val actual = sut.findByProviderId(providerId)

                actual shouldBe expected

                verify {
                    sut.findByProviderId(providerId)
                }
            }
        }

        `when`("providerId에 해당하는 회원이 없다면") {
            val providerId = "테스트 프로바이더 아이디"
            val expected = null

            every { sut.findByProviderId(providerId) }.returns(expected)

            then("null을 반환한다.") {
                val actual = sut.findByProviderId(providerId)

                actual shouldBe expected

                verify {
                    sut.findByProviderId(providerId)
                }
            }
        }
    }
})
