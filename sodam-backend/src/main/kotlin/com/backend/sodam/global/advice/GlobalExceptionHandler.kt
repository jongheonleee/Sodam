package com.backend.sodam.global.advice

import com.backend.sodam.global.commons.ErrorCode
import com.backend.sodam.global.commons.SodamApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

// 참고 : https://cheese10yun.github.io/spring-guide-exception/
@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    // 공통 RuntimeException 핸들링
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): SodamApiResponse<*> {
        log.error("error={}", e.message, e)
        return SodamApiResponse.fail(ErrorCode.DEFAULT_ERROR, e.message.toString()) // 추후에 상태 코드 어디에서 처리할지 고민
    }
}
