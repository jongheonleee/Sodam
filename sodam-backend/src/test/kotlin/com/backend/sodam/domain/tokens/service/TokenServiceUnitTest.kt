package com.backend.sodam.domain.tokens.service

import com.backend.sodam.domain.tokens.exception.TokenException
import com.backend.sodam.domain.tokens.service.dto.TokenResponse
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.service.response.UserResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows

class TokenServiceUnitTest : BehaviorSpec({

    // 제공되는 기능
    // - 1. accessToken으로 회원 조회
    // - 2. 카카오로부터 토큰 발급 받기
    // - 3. 토큰 유효성 검증
    // - 4. 회원 토큰 발급
    // - 5. 소셜 회원 토큰 발급
    // - 6. 소셜 회원의 토큰 upsert(존재하면 업데이트, 없으면 생성)
    // - 7. 회원의 토큰 upsert
    val sut = mockk<TokenService>()

    // - 1. accessToken으로 회원 조회
    // - 회원 조회에 성공
    // - 토큰에서 회원 아이디를 조회하지 못해서 UserIdNotFoundOnTokenException() 예외 발생
    // - 토큰에서 회원 아이디를 조회했지만, 해당 회원 아이디에 해당하는 회원이 없는 경우 UserNotFoundException() 예외 발생
    given("클라이언트로부터 accessToken으로 회원 조회 요청을 받았을 때") {
        `when`("토큰에서 회원 아이디를 조회했고 해당 회원 아이디에 해당하는 회원이 있는 경우") {
            val token = "테스트 토큰"
            val expected = UserResponse(
                userId = "테스트 유저 아이디" ,
                email = "테스트 유저 이메일",
                name = "테스트 유저",
                password = "테스트 유저 비밀번호",
                profileImage = "테스트 유저 프로필 이미지 url",
                introduce = "테스트 유저 자기소개글"
            )

            every { sut.findUserByAccessToken(token) } returns expected

            then("회원 정보를 반환한다.") {

                val actual = sut.findUserByAccessToken(token)

                actual shouldBe expected

                verify { sut.findUserByAccessToken(token) }
            }
        }

        `when`("토큰에서 회원 아이디를 조회하지 못한 경우") {
            val token = "테스트 토큰"
            val expected = TokenException.InvalidTokenException()

            every { sut.findUserByAccessToken(token) }.throws(expected)

            then("UserIdNotFoundOnTokenException 예외가 발생한다.") {
                assertThrows<TokenException.InvalidTokenException> {
                    sut.findUserByAccessToken(token)
                }

                verify { sut.findUserByAccessToken(token) }
            }
        }

        `when`("토큰에서 회원 아이디를 조회했지만 해당 아이디에 대한 회원이 없는 경우") {
            val token = "테스트 토큰"
            val expected = UserException.UserNotFoundException()

            every { sut.findUserByAccessToken(token) }.throws(expected)

            then("UserNotFoundException 예외가 발생한다.") {
                assertThrows<UserException.UserNotFoundException> {
                    sut.findUserByAccessToken(token)
                }

                verify { sut.findUserByAccessToken(token) }
            }
        }
    }

    // - 2. 카카오로부터 토큰 발급 받기
    // - 카카오로부터 토큰값을 받는다
    given("클라이언트로부터 카카오에서 생성 토큰을 요청할 때") {
        `when`("카카오로부터 성공적으로 토큰 값을 전달 받으면") {
            val code = "테스트 코드"
            val expected = "카카오 발급 토큰"

            every { sut.getTokenFromKakao(code) } returns expected
            then("토큰 값을 반환한다.") {
                val actual = sut.getTokenFromKakao(code)

                actual shouldBe expected

                verify { sut.getTokenFromKakao(code) }
            }
        }
    }

    // - 3. 토큰 유효성 검증
    // - 유효한 토큰으로 파싱된 경우 true를 반환한다.
    // - 토큰 파싱 과정 중에 예외가 발생하면 false를 반환한다.
    given("클라이언트로부터 토큰 유효성 검증을 요청할 때") {
        `when`("해당 토큰을 성공적으로 파싱하면") {
            val accessToken = "테스트 토큰"

            every { sut.validateToken(accessToken) } returns true

            then("true를 반환한다.") {
                val actual = sut.validateToken(accessToken)

                actual shouldBe true

                verify { sut.validateToken(accessToken) }
            }
        }

        `when`("파싱 과정에서 예외가 발생하면") {
            val accessToken = "테스트 토큰"

            every { sut.validateToken(accessToken) } returns false

            then("false를 반환한다.") {
                val actual = sut.validateToken(accessToken)

                actual shouldBe false

                verify { sut.validateToken(accessToken) }
            }
        }
    }

    // - 4. 회원 토큰 발급
    // - 회원 이메일 기반으로 토큰을 발급함
    given("클라이언트로부터 회원 이메일 기반 토큰 발급을 요청할 때") {

        `when`("토큰을 성공적으로 생성하면") {
            val email = "test@test.com"
            val expected = TokenResponse(
                accessToken = "테스트 access token",
                refreshToken = "테스트 refresh token",
            )

            every { sut.createNewTokenForUser(email) } returns expected

            then("생성된 토큰을 반환한다.") {
                val actual = sut.createNewTokenForUser(email)

                actual shouldBe expected

                verify { sut.createNewTokenForUser(email) }
            }
        }
    }

    // - 5. 소셜 회원 토큰 발급
    // - 소셜 회원 아이디 기반으로 토큰을 발급함
    given("클라이언트로부터 소셜 회원 아이디 기반 토큰 발급을 요청할 때") {

        `when`("토큰을 성공적으로 생성하면") {
            val userId = "테스트 아이디"
            val expected = TokenResponse(
                accessToken = "테스트 access token",
                refreshToken = "테스트 refresh token",
            )

            every { sut.createNewTokenForSocialUser(userId) } returns expected

            then("생성된 토큰을 반환한다.") {
                val actual = sut.createNewTokenForSocialUser(userId)

                actual shouldBe expected

                verify { sut.createNewTokenForSocialUser(userId) }
            }
        }
    }

    // - 6. 소셜 회원의 토큰 upsert(존재하면 업데이트, 없으면 생성)
    // - accessToken 을 성공적으로 반환한다
    given("클라이언트로부터 소셜 회원 providerId로 토큰 upsert를 요청할 때") {

        `when`("토큰을 성공적으로 upsert하면") {
            val providerId = "테스트 프로바이더 아이디"
            val expected = TokenResponse(
                accessToken = "테스트 발급 토큰",
                refreshToken = "테스트 리프레쉬 토큰",
            )

            every { sut.upsertTokenForSocialUser(providerId) } returns expected

            then("accessToken을 반환한다.") {
                val actual = sut.upsertTokenForSocialUser(providerId)

                actual shouldBe expected

                verify { sut.upsertTokenForSocialUser(providerId) }
            }
        }
    }


    // - 7. 회원의 토큰 upsert(존재하면 업데이트, 없으면 생성)
    // - accessToken 을 성공적으로 반환한다
    given("클라이언트로부터 회원 email로 토큰 upsert를 요청할 때") {

        `when`("토큰을 성공적으로 upsert하면") {
            val email = "test@test.com"
            val expected = TokenResponse(
                accessToken = "테스트 발급 토큰",
                refreshToken = "테스트 리프레쉬 토큰",
            )

            every { sut.upsertTokenForUser(email) } returns expected

            then("accessToken을 반환한다.") {
                val actual = sut.upsertTokenForUser(email)

                actual shouldBe expected

                verify { sut.upsertTokenForUser(email) }
            }
        }
    }
})