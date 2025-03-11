package com.backend.sodam.domain.users.service

import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.service.dto.SignupRequestDto
import com.backend.sodam.domain.users.service.dto.SignupResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows

class AuthServiceTest : BehaviorSpec({

    val sut = mockk<UserService>();

    given("클라이언트로부터 회원가입 요청을 받았을 때") {
        `when`("1. 중복되는 이메일이 없다면") {
            // 전달 받은 데이터, 반환할 데이터 세팅
            val dto = SignupRequestDto(
                email = "asdf1234@gmail.com",
                name = "테스트 유저",
                password = "asdf1234",
                profileImage = "aws에 등록된 프로필 이미지",
                introduce = "테스트 자기소개글"
            )

            val expected = SignupResponse(
                userId = "생성된 uuid",
                email = "asdf1234@gmail.com",
                name = "테스트 유저"
            )

            // 목킹
            every { sut.signupUser(dto) } returns expected

            then("생성된 uuid와 전달 받은 이름, 이메일을 포함하는 응답 객체를 반환한다.") {
                // 실제 호출
                val actual = sut.signupUser(dto)

                // 내용 비교
                actual.email shouldBe expected.email
                actual.name shouldBe expected.name
                // actual.userId shouldBe expected.userId

                // 호출 검증
                verify {
                    sut.signupUser(dto)
                }
            }
        }

        `when`("2. 중복되는 이메일이 있다면") {
            // 전달 받은 데이터, 반환할 데이터 세팅
            val dto = SignupRequestDto(
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
    }

})
