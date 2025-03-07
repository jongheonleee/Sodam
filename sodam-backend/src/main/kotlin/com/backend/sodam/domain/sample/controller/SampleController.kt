package com.backend.sodam.domain.sample.controller

import com.backend.sodam.domain.sample.service.SampleService
import com.backend.sodam.global.commons.SodamApiResponse
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class SampleController(
    private val sampleService: SampleService,
) {
    @GetMapping("/sample")
    fun sampleGet(): SodamApiResponse<String> {
        val sample = sampleService.getSample("sample")
        return SodamApiResponse.ok(sample.name)
    }

    @PostMapping("/sample")
    fun samplePost(
        @RequestParam("name") name: String
    ): SodamApiResponse<String> {
        val sample = sampleService.createSample(name)
        return SodamApiResponse.ok(sample.name)
    }
}
