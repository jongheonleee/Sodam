package com.backend.sodam.domain.grades.repository

import com.backend.sodam.domain.grades.exception.GradeException
import com.backend.sodam.domain.users.entity.UsersGradeEntity
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import com.backend.sodam.domain.users.repository.UserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Repository
@RequiredArgsConstructor
class UserGradeRepository(
    private val userJpaRepository: UserJpaRepository,
    private val socialUserJpaRepository: SocialUserJpaRepository,
    private val gradeJpaRepository: GradesJpaRepository,
    private val useGradeJpaRepository: UserGradeJpaRepository,
) {

    @Transactional
    fun createGradeForUser(userId: String, gradeName: String): UsersGradeEntity { // 상황봐서 도메인 객체 넘길지 판단
        val byUserId = userJpaRepository.findByUserId(userId)
        if (byUserId.isEmpty) {
            throw UserException.UserNotFoundException()
        }

        val userEntity = byUserId.get()
        val byGradeName = gradeJpaRepository.findByGradeName(gradeName)
        if (byGradeName.isEmpty) {
            throw GradeException.GradeNotFoundException()
        }
        val gradeEntity = byGradeName.get()

        val useGradeEntity = UsersGradeEntity(
            userGradeId = UUID.randomUUID().toString(),
            user = userEntity,
            grade = gradeEntity,
            startAt = LocalDateTime.now(),
            endAt = LocalDateTime.of(9999, 12, 31, 23, 59, 59),
            validYN = 0,
        )

        return useGradeJpaRepository.save(useGradeEntity)

    }

    @Transactional
    fun createGradeForSocialUser(socialUserId: String, gradeName: String): UsersGradeEntity {
        val bySocialUserId = socialUserJpaRepository.findBySocialUserId(socialUserId)
        if (bySocialUserId.isEmpty) {
            throw UserException.UserNotFoundException()
        }
        val socialUserEntity = bySocialUserId.get()

        val byGradeName = gradeJpaRepository.findByGradeName(gradeName)
        if (byGradeName.isEmpty) {
            throw GradeException.GradeNotFoundException()
        }
        val gradeEntity = byGradeName.get()


        val useGradeEntity = UsersGradeEntity(
            userGradeId = UUID.randomUUID().toString(),
            socialUser = socialUserEntity,
            grade = gradeEntity,
            startAt = LocalDateTime.now(),
            endAt = LocalDateTime.of(9999, 12, 31, 23, 59, 59),
            validYN = 0,
        )

        return useGradeJpaRepository.save(useGradeEntity)

    }
}