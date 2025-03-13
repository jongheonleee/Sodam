package com.backend.sodam.domain

import com.backend.sodam.global.commons.SodamApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/api/v1/hello")
    fun hello(): SodamApiResponse<String> {
        return SodamApiResponse.ok("연결되었습니다.")
    }
}
