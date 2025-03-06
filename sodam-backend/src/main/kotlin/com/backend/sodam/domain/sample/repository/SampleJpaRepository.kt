package com.backend.sodam.domain.article.repository

import com.backend.sodam.domain.article.entity.SampleEntity
import org.springframework.data.jpa.repository.JpaRepository


interface SampleJpaRepository : JpaRepository<SampleEntity, String> {
}
