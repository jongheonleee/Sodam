package com.backend.sodam.global.security

import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class SodamUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(email: String): SodamAuthUser {
        // 이메일로 회원을 조회함
        val foundUserByEmail = userRepository.findByUserEmail(email)
            .orElseThrow { UserException.UserNotFoundException() }

        println("foundUserByEmail의 비밀번호 : ${foundUserByEmail.password}")
        // 조회 성공하면 인증 객체 반환
        return SodamAuthUser(
            userId = foundUserByEmail.userId,
            username = foundUserByEmail.name,
            email = foundUserByEmail.email,
            password = foundUserByEmail.password,
            authorities = listOf(SimpleGrantedAuthority("ROLE_USER")) // 추후에 ROLE 추가해야함
        )
    }
}
