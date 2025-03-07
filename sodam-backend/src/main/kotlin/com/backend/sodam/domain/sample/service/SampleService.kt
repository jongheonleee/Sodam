package com.backend.sodam.domain.sample.service

import com.backend.sodam.domain.sample.repository.SampleRepository
import com.backend.sodam.domain.sample.service.dto.SampleResponse
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class SampleService(
    private val sampleRepository: SampleRepository
) {
    fun getSample(name: String): SampleResponse {
        val sampleName = sampleRepository.getSampleName(name)
        return SampleResponse(sampleName)
    }

    fun createSample(name: String): SampleResponse {
        return SampleResponse(sampleRepository.createSample(name))
    }
}
