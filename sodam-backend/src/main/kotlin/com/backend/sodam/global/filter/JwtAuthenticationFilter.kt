package com.backend.sodam.global.filter

import com.backend.sodam.domain.tokens.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

// jwt 토큰 유효성 처리 필터
@Component
@RequiredArgsConstructor
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val tokenService: TokenService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = resolveToken(request)
        if (!StringUtils.isEmpty(token) && tokenService.validateToken(token)) {
            val authentication = jwtTokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String {
        val bearerToken = request.getHeader("Authorization")
        return bearerToken.takeIf { StringUtils.hasText(it) && it.startsWith("Bearer ") }?.substring(7) ?: ""
    }
}