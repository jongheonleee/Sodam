package com.backend.sodam.global.interceptor

import com.backend.sodam.global.authentication.AuthenticationHolder
import com.backend.sodam.global.authentication.RequestedBy
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import org.springframework.ui.ModelMap
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.WebRequestInterceptor
import java.lang.Exception

const val REQUESTED_BY_HEADER = "requested-by"

@Component
@RequiredArgsConstructor
class RequestedByInterceptor(
    val authenticationHolder: AuthenticationHolder
) : WebRequestInterceptor {

    override fun preHandle(request: WebRequest) {
        val requestedBy = request.getHeader(REQUESTED_BY_HEADER)
        val requested = requestedBy?.let { RequestedBy(it) }
        requested?.let { authenticationHolder.setAuthentication(it) } // requestedBy가 null이면 아래 코드 호출되지 않음
    }

    override fun postHandle(request: WebRequest, model: ModelMap?) {
    }

    override fun afterCompletion(request: WebRequest, ex: Exception?) {
    }
}
