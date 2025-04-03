package com.backend.sodam.domain.grades.service.port

import com.backend.sodam.domain.grades.model.SodamGrade
import java.util.*

interface FetchGradePort {
    fun isExistsByGradeName(gradeName: String): Boolean
    fun findValidGradeByName(name: String): Optional<SodamGrade>
}