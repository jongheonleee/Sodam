package com.backend.sodam.global.config

import com.backend.sodam.global.interceptor.RequestedByInterceptor
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
@EnableWebMvc
@RequiredArgsConstructor
class RequestedByMvcConfigurer(
    val interceptor: RequestedByInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addWebRequestInterceptor(interceptor)
    }
}
