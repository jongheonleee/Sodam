package com.backend.sodam.domain.grades.repository

import com.backend.sodam.domain.grades.model.SodamGrade
import java.util.Optional

interface GradesCustomRepository {
    fun findValidGradeByName(name: String): Optional<SodamGrade>
}