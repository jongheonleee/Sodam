package com.backend.sodam.domain.article.repository

import com.backend.sodam.domain.article.entity.SampleEntity
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class SampleRepository(
    val sampleJpaRepository: SampleJpaRepository,
){
    @Transactional
    fun getSampleName(id: String) : String {
        val byId : Optional<SampleEntity> = sampleJpaRepository.findById(id)
        return byId.map(SampleEntity::sampleName).orElseThrow()
    }
}
