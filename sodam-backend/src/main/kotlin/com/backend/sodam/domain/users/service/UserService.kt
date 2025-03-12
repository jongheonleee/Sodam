package com.backend.sodam.domain.users.service

import com.backend.sodam.domain.subscriptions.repository.UserSubscriptionRepository
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.UserRepository
import com.backend.sodam.domain.users.service.dto.SignupRequestDto
import com.backend.sodam.domain.users.service.dto.SignupResponse
import com.backend.sodam.domain.users.service.dto.UserResponse
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class UserService(
    private val userRepository: UserRepository,
    private val userSubscriptionRepository: UserSubscriptionRepository,
) {

    // - 중복 이메일을 확인한다.
    //    - 중복된 이메일이 존재하는 경우, 알림 메시지 반환
    // - 회원가입 처리를 한다.
    // - 무료 구독권을 생성한다.
    fun signupUser(signupRequestDto: SignupRequestDto) : SignupResponse {
        userRepository.findByUserEmail(signupRequestDto.email)
                      .ifPresent { throw UserException.UserAlreadyExistsException() }

        val savedUserResponse = userRepository.create(signupRequestDto)
        userSubscriptionRepository.create(savedUserResponse.userId)
        return savedUserResponse
    }

    fun findByUserEmail(email: String) : UserResponse {
        return userRepository.findByUserEmail(email)
                            .orElseThrow { UserException.UserAlreadyExistsException() }
    }
}