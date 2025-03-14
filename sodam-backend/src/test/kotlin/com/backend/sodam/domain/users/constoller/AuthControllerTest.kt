package com.backend.sodam.domain.users.constoller

import com.backend.sodam.domain.tokens.service.TokenService
import com.backend.sodam.domain.users.controller.AuthController
import com.backend.sodam.domain.users.service.UserService
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder


@WebMvcTest(AuthController::class)
class AuthControllerTest : DescribeSpec({
    // 목킹 대상
    @MockK lateinit var userService: UserService
    @MockK lateinit var authenticationManagerBuilder: AuthenticationManagerBuilder
    @MockK lateinit var tokenService: TokenService

    beforeEach {
        // 목킹 객체 초기화
        clearAllMocks()

        // 테스트 진행 전 처리할 작업
    }


    context("[POST] /api/v1/auth/signup -> 회원가입 요청 처리") {
        // 1. 사용자가 올바른 요청 데이터를 전달하면 회원가입에 성공한다.
        describe("사용자가 올바른 요청 데이터를 전달하면") {
            // 목킹 처리
            every {

            }

            // 요청 데이터, 응답 데이터

            it("회원가입에 성공해야한다.") {

            }
        }
        // 2. 사용자가 잘못된 요청 데이터를 전달하면 예외가 발생한다.
        describe("사용자가 잘못된 요청 데이터를 전달하면") {
            // 목킹 처리
            every {

            }

            // 요청 데이터, 응답 데이터
            it("예외가 발생한다.") {

            }
        }

        // 3. 이미 존재하는 이메일의 경우 UserAlreadyExistsException 예외가 발생한다.
        describe("사용자가 전달한 이메일이 이미 존재하면") {
            // 목킹 처리
            every {

            }

            // 요청 데이터, 응답 데이터
            it(" UserAlreadyExistsException 예외가 발생한다.") {

            }
        }
    }

    context("[POST] /api/v1/auth/login -> 로그인 요청 처리") {
        // 1. 사용자가 유효한 이메일, 비밀번호를 전달하면 인증에 성공한다.
        describe("사용자가 유효한 이메일, 비밀번호를 전달하면면") {
            // 목킹 처리
            every {

            }

            // 요청 데이터, 응답 데이터

            it("인증에 성공한다.") {

            }
        }
        // 2. 사용자가 유효하지 않은 이메일, 비밀번호를 전달하면 예외가 발생한다.
        describe("사용자가 유효하지 않은 이메일, 비밀번호를 전달하면") {
            // 목킹 처리
            every {

            }

            // 요청 데이터, 응답 데이터

            it(" 예외가 발생한다.") {

            }
        }
        // 3. 사용자가 등록되지 않은 이메일을 전달하면 예외가 발생한다.
        describe("사용자가 등록되지 않은 이메일을 전달하면") {
            // 목킹 처리
            every {

            }

            // 요청 데이터, 응답 데이터

            it(" 예외가 발생한다.") {

            }
        }
        // 4. 사용자가 잘못된 비밀번호를 전달하면 예외가 발생한다.
        describe("사용자가 잘못된 비밀번호를 전달하면") {
            // 목킹 처리
            every {

            }

            // 요청 데이터, 응답 데이터

            it(" 예외가 발생한다.") {

            }
        }
    }

    context("[POST] /api/v1/auth/callback -> oauth2 로그인 요청 처리") {
        // 1. 사용자가 이미 소셜 회원이 있고 oauth2로 로그인에 성공한다.
        describe("사용자가 이미 소셜 회원이 있고 oauth2로 로그인 요청을 보내면") {
            // 목킹 처리
            every {

            }

            // 요청 데이터, 응답 데이터

            it("인증에 성공한다.") {

            }
        }
        // 2. 사용자가 소셜 회원이 없다면 소셜 회원을 생성하고 oauth2로 로그인에 성공한다.
        describe("사용자가 소셜 회원이 없다면 소셜 회원을 생성하고 oauth2로 로그인 요청을 보내면") {
            // 목킹 처리
            every {

            }

            // 요청 데이터, 응답 데이터

            it("인증에 성공한다.") {

            }
        }
    }
})