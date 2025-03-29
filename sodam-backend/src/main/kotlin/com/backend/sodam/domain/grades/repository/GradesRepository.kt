package com.backend.sodam.domain.grades.repository

import com.backend.sodam.domain.grades.model.SodamGrade
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class GradesRepository(
    private val gradeJpaRepository: GradesJpaRepository,
) {

    @Transactional(readOnly = true)
    fun findValidGradeByName(name: String): Optional<SodamGrade> {
        return gradeJpaRepository.findValidGradeByName(name)
    }

}