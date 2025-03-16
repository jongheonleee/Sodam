package com.backend.sodam.global.filter

import com.backend.sodam.domain.users.service.UserHistoryService
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

@Component
@RequiredArgsConstructor
class UserHistoryLoggingFilter(
    private val userHistoryService: UserHistoryService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        // 로그 찍는 부분 별도의 스레드에서 돌아가게끔 구현
        // 비동기 처리
        CompletableFuture.runAsync {
            log(authentication, request)
        }
        filterChain.doFilter(request, response)
    }

    fun log(authentication: Authentication, request: HttpServletRequest) {
        userHistoryService.log(
            userId = authentication.name,
            userRole = authentication.authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")),
            clientIp = request.remoteAddr,
            reqMethod = request.method,
            reqUrl = request.requestURI,
            reqHeader = getHeader(request),
            reqPayload = "payload"
        )
    }

    fun getHeader(request: HttpServletRequest): String {
        // 헤더를 저장할 Map 생성
        val headersMap = hashMapOf<String, String>()

        // 모든 헤더 추출
        val headerNames = request.headerNames
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                val headerName = headerNames.nextElement()
                val headerValue = request.getHeader(headerName)
                headersMap[headerName] = headerValue
            }
        }

        // Map을 JSON 문자열로 변환
        return try {
            val objectMapper = ObjectMapper()
            objectMapper.writeValueAsString(headersMap)
        } catch (e : JsonProcessingException) {
            "{}"
        }
    }
}