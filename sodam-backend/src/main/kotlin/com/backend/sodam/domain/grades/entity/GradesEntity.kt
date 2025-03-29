package com.backend.sodam.domain.grades.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "grades")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class GradesEntity(
    @Id
    @Column(name = "GRADE_ID")
    val gradeId: String,

    @Column(name = "GRADE_NAME")
    val gradeName: String,

    @Column(name = "GRADE_ORD")
    val gradeOrd: Int,

    @Column(name = "GRADE_SUMMARY")
    val gradeSummary: String,

    @Column(name = "GRADE_DESCRIBE")
    val gradeDescription: String,

    // 가변 필드
    validYN: Int
) : MutableBaseEntity() {

    @Column(name = "VALID_YN")
    var validYN: Int = validYN
        protected set
}
