package com.backend.sodam.domain.article.service

import com.backend.sodam.domain.article.repository.SampleJpaRepository
import com.backend.sodam.domain.article.repository.SampleRepository
import com.backend.sodam.domain.article.service.dto.SampleResponse
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class SampleService(
    val sampleRepository: SampleRepository,
) {
    fun getSample() : SampleResponse {
        val sampleName = sampleRepository.getSampleName("1")
        return SampleResponse(sampleName)
    }
}
