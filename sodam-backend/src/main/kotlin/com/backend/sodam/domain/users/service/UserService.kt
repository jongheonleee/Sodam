package com.backend.sodam.domain.users.service

import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.UserJpaRepository
import com.backend.sodam.domain.users.service.dto.SignupRequestDto
import com.backend.sodam.domain.users.service.dto.SignupResponse
import com.backend.sodam.domain.users.service.dto.toEntity
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class UserService(
    private val userJpaRepository: UserJpaRepository,
) {

    fun signupUser(signupRequestDto: SignupRequestDto) : SignupResponse {
        // - 중복 이메일을 확인한다.
        //    - 중복된 이메일이 존재하는 경우, 알림 메시지 반환
        // - 회원가입  처리를 한다.
        // - 회원가입 완료한 회원 정보를 반환한다
        val foundUserByEmail = userJpaRepository.findByUserEmail(signupRequestDto.email)
        if (foundUserByEmail.isPresent) {
            throw UserException.UserAlreadyExistsException()
        }

        val signupRequestUserEntity = signupRequestDto.toEntity()
        val savedUser = userJpaRepository.save(signupRequestUserEntity)

        return SignupResponse(
            userId = savedUser.userId,
            email = savedUser.userEmail,
            name = savedUser.userName,
        )

    }
}