package com.backend.sodam.domain.grades.repository

import com.backend.sodam.domain.grades.model.SodamGrade
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@RequiredArgsConstructor
class GradesCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): GradesCustomRepository {

    @Transactional(readOnly = true)
    override fun findValidGradeByName(name: String): Optional<SodamGrade> {
        TODO("Not yet implemented")
    }
}