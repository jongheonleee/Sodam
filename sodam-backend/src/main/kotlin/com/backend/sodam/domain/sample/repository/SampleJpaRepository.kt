package com.backend.sodam.domain.sample.repository

import com.backend.sodam.domain.sample.entity.SampleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SampleJpaRepository : JpaRepository<SampleEntity, String>
