package com.backend.sodam.domain.grades.service.port

import com.backend.sodam.domain.grades.model.GradesType
import com.backend.sodam.domain.users.model.UserType

interface CreateUserGradePort {
    fun isTarget(userType: UserType): Boolean
    fun createGrade(userId: String, gradeType: GradesType)
}