package com.backend.sodam.domain.sample.controller

import com.backend.sodam.domain.sample.service.SampleService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class SampleController(
    val sampleService: SampleService
) {
    @GetMapping("/sample", produces = ["text/plain;charset=UTF-8"]) // 현재 응답 헤더가 "text/plain;charset=UTF-8" 아니여서 인코딩 문제 발생. 따라서, produces로 지정
    fun sampleGet(): String {
        val sample = sampleService.getSample("sample")
        return "this is a sample test [GET] name : ${sample.name}"
    }

    @PostMapping("/sample", produces = ["text/plain;charset=UTF-8"])
    fun samplePost(
        @RequestParam("name") name: String
    ): String {
        return "this is a sample test [POST] name : ${sampleService.createSample(name)}"
    }
}
