// package com.backend.sodam.domain.users.integration
//
// import com.backend.sodam.domain.users.service.dto.SignupResponse
// import com.fasterxml.jackson.databind.ObjectMapper
// import io.kotest.core.spec.style.BehaviorSpec
// import io.kotest.matchers.shouldBe
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.boot.test.context.SpringBootTest
// import org.springframework.boot.test.web.client.TestRestTemplate
//
// // 추후에 구독권 생성 엔드포인트 개발되면 적용
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// class AuthIntegrationTest @Autowired constructor(
//    private val restTemplate: TestRestTemplate,
//    private val objectMapper: ObjectMapper
// ) : BehaviorSpec({
//
// //    // 무료 구독권 미리 등록
// //    beforeEach({
// //        val subscriptionRequest = mapOf(
// //            "name" to "무료 구독권",
// //            "content" to "모든 회원에게 제공되는 무료 구독권, 구독 서비스 이용 혜택 없음",
// //            "viewCnt" to 0,
// //            "downCnt" to 0,
// //        )
// //
// //        restTemplate.postForEntity(
// //            "/api/v1/subscriptions",
// //            subscriptionRequest,
// //            String::class.java
// //        )
// //
// //    })
//
//    given("POST api/v1/auth/signup - 회원가입을 요청 테스트.", {
//        // 요청 데이터
//        val signupRequest = mapOf(
//            "email" to "testuser123@gmail.com",
//            "name" to "테스트 유저",
//            "password" to "asdf12341234@",
//            "introduce" to "안녕하새요 테스트 유저입니다.",
//            "profileImage" to "dddd"
//        )
//        `when`("사용자가 정상적인 경로로 올바른 데이터를 보내면") {
//            // 회원가입 처리
//            val response = restTemplate.postForEntity(
//                "/api/v1/auth/signup",
//                signupRequest,
//                String::class.java
//            )
//
//            then("성공적으로 회원가입이 되어 회원과 무료 구독권이 발급된다") {
//                // 응답 데이터 확인
//                response.statusCode.value() shouldBe 201
//
//                val signupResponse = objectMapper.readValue(response.body, SignupResponse::class.java)
//
//                signupResponse.name shouldBe "홍길동"
//                signupResponse.email shouldBe "testuser123@gmail.com"
//            }
//        }
//    })
// })
