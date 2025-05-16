package com.backend.sodam.domain.grades.repository

import com.backend.sodam.domain.grades.model.GradesType
import com.backend.sodam.domain.grades.service.port.CreateUserGradePort
import com.backend.sodam.domain.grades.service.port.DeleteUserGradePort
import com.backend.sodam.domain.grades.service.port.FetchUserGradePort
import com.backend.sodam.domain.grades.service.port.UpdateUserGradePort
import com.backend.sodam.domain.users.entity.UsersGradeEntity
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Repository
@RequiredArgsConstructor
class SocialUserGradeRepository(
    private val gradeJpaRepository: GradesJpaRepository,
    private val useGradeJpaRepository: UserGradeJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository
) : CreateUserGradePort, FetchUserGradePort, UpdateUserGradePort, DeleteUserGradePort {

    override fun isTarget(userType: UserType): Boolean =
        UserType.SOCIAL == userType

    @Transactional
    override fun createGrade(userId: String, gradeType: GradesType) {
        val socialUserEntity = socialUserJpaRepository.findBySocialUserId(userId).get()
        val gradeEntity = gradeJpaRepository.findByGradeName(gradeType.name).get()
        val useGradeEntity = UsersGradeEntity(userGradeId = UUID.randomUUID().toString(), socialUser = socialUserEntity, grade = gradeEntity, startAt = LocalDateTime.now(), endAt = LocalDateTime.of(9999, 12, 31, 23, 59, 59), validYN = 0)
        useGradeJpaRepository.save(useGradeEntity)
    }
}
