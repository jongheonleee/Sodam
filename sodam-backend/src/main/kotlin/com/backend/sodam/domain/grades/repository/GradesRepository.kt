package com.backend.sodam.domain.grades.repository

import com.backend.sodam.domain.grades.model.SodamGrade
import com.backend.sodam.domain.grades.service.port.FetchGradePort
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class GradesRepository(
    private val gradeJpaRepository: GradesJpaRepository
) : FetchGradePort {

    @Transactional(readOnly = true)
    override fun isExistsByGradeName(gradeName: String): Boolean =
        gradeJpaRepository.existsByGradeName(gradeName)

    @Transactional(readOnly = true)
    override fun findValidGradeByName(name: String): Optional<SodamGrade> {
        return gradeJpaRepository.findValidGradeByName(name)
    }
}
