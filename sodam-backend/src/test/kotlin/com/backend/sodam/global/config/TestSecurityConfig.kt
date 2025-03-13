package com.backend.sodam.global.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@TestConfiguration
class TestSecurityConfig {

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity.httpBasic { it.disable() }
        httpSecurity.formLogin { it.disable() }
        httpSecurity.authorizeHttpRequests { it.anyRequest().permitAll() }
            .csrf { it.disable() }
        return httpSecurity.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder() // 개발 단계 이므로 BCryptPasswordEncoder 사용
    }
}
