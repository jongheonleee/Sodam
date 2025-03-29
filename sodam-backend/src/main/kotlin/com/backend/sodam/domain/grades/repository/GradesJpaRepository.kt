package com.backend.sodam.domain.grades.repository

import com.backend.sodam.domain.grades.entity.GradesEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface GradesJpaRepository: JpaRepository<GradesEntity, String>, GradesCustomRepository {
    fun findByGradeName(gradeName: String): Optional<GradesEntity>
}