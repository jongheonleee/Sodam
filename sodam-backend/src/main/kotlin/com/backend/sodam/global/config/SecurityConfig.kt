package com.backend.sodam.global.config

import com.backend.sodam.global.filter.JwtAuthenticationFilter
import com.backend.sodam.global.filter.UserHistoryLoggingFilter
import com.backend.sodam.global.security.SodamUserDetailsService
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.session.web.http.CookieSerializer
import org.springframework.session.web.http.DefaultCookieSerializer
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
class SecurityConfig(
    private val sodamUserDetailsService: SodamUserDetailsService,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val userHistoryLoggingFilter: UserHistoryLoggingFilter
) {

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        // 프론트엔드 리액트로 개발했기 때문에 아래 disable 시키기
        httpSecurity.cors { it.configurationSource(corsConfigurationSource()) }
        httpSecurity.httpBasic { it.disable() }
        httpSecurity.formLogin { it.disable() }
        httpSecurity.csrf { it.disable() }

        // 로그인, 회원가입, 이외의 모든 요청 인증 요구
        httpSecurity.authorizeHttpRequests {
            it.requestMatchers(
                "/api/v1/auth/signup", // 회원가입
                "/api/v1/auth/login", // 로그인
                "/api/v1/auth/callback", // oauth2 로그인 요청
                "/swagger-ui/**", // Swagger UI 경로
                "/v3/api-docs/**", // Swagger API 문서 경로
                "/api/v1/positions" // 회원가입시 포지션 조회
            )
                .permitAll()
                .anyRequest()
                .authenticated()
        }

        // oauth 관련 설정 -> 추후에 oauth2 적용할 때 활용할 예정
        httpSecurity.oauth2Login { it.failureUrl("/login?error=true") }

        // userDetails 조회 빈 등록
        httpSecurity.userDetailsService(sodamUserDetailsService)

        // jwt 토큰 필터 추가(해당 필터 앞단에 추가함) -> 인증 이전에 처리해야함
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        // 회원 이력 처리 필터 추가 -> 인증 이후에 처리해야함
        httpSecurity.addFilterAfter(userHistoryLoggingFilter, UsernamePasswordAuthenticationFilter::class.java)

        return httpSecurity.build()
    }

    fun corsConfigurationSource(): CorsConfigurationSource {
        return CorsConfigurationSource {
            val configuration = CorsConfiguration()
            configuration.allowedHeaders = listOf("*")
            configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
            configuration.allowedOriginPatterns = listOf("https://localhost:3000" )
            configuration.allowCredentials = true
            configuration
        }
    }

    @Bean
    fun cookieSerializer(): CookieSerializer {
        val serializer = DefaultCookieSerializer()
        serializer.setSameSite("None")
        serializer.setUseSecureCookie(true)
        return serializer
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder() // 개발 단계 이므로 BCryptPasswordEncoder 사용
    }
}
