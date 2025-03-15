package com.backend.sodam.global.filter

import com.backend.sodam.domain.tokens.service.TokenService
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component

@Component
@RequiredArgsConstructor
class JwtTokenProvider(
    private val tokenService: TokenService,
) {

    fun getAuthentication(accessToken: String): Authentication {
        val foundUserByAccessToken = tokenService.findUserByAccessToken(accessToken)

        val authorities = listOf<SimpleGrantedAuthority>(
            SimpleGrantedAuthority(foundUserByAccessToken.role)
        )

        val principal = User(
            foundUserByAccessToken.name,
            foundUserByAccessToken.password,
            authorities
        )

        return UsernamePasswordAuthenticationToken(
            principal,
            foundUserByAccessToken.userId,
            authorities
        )
    }
}