package com.backend.sodam.global.config

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
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
class SecurityConfig(
    private val sodamUserDetailsService: SodamUserDetailsService
) {

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        // 프론트엔드 리액트로 개발했기 때문에 아래 disable 시키기
        httpSecurity.httpBasic { it.disable() }
        httpSecurity.formLogin { it.disable() }
        httpSecurity.csrf { it.disable() }
        httpSecurity.cors { it.configurationSource(corsConfigurationSource()) }

        // 개발 단계 이므로 모든 요청 열어두기
        httpSecurity.authorizeHttpRequests {
            it.requestMatchers(
                "/api/v1/auth/signup", // 회원가입
                "/api/v1/auth/login", // 로그인
                "/api/v1/auth/callback" // oauth2 로그인 요청
            )
                .permitAll()
                .anyRequest()
                .authenticated()
        }

        // oauth 관련 설정 -> 추후에 oauth2 적용할 때 활용할 예정
        httpSecurity.oauth2Login { it.failureUrl("/login?error=true") }

        httpSecurity.userDetailsService(sodamUserDetailsService)
        return httpSecurity.build()
    }

    fun corsConfigurationSource(): CorsConfigurationSource {
        return CorsConfigurationSource {
            val configuration = CorsConfiguration()
            configuration.allowedHeaders = listOf("*")
            configuration.allowedMethods = listOf("*")
            configuration.allowedOriginPatterns = listOf("*")
            configuration.allowCredentials = true
            configuration
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder() // 개발 단계 이므로 BCryptPasswordEncoder 사용
    }
}
