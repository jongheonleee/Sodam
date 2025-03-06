package com.backend.sodam.domain.sample.repository

import com.backend.sodam.domain.sample.entity.SampleEntity
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class SampleRepository(
    val sampleJpaRepository: SampleJpaRepository
) {
    @Transactional
    fun getSampleName(id: String): String {
        val foundSampleEntity: Optional<SampleEntity> = sampleJpaRepository.findById(id)
        println(foundSampleEntity.get())
        return foundSampleEntity.map(SampleEntity::sampleName).orElseThrow()
    }

    @Transactional
    fun createSample(name: String): String {
        val sampleEntity: SampleEntity = SampleEntity(
            UUID.randomUUID().toString(),
            name,
            ""
        )
        val savedSampleEntity = sampleJpaRepository.save(sampleEntity)
        return name
    }
}
