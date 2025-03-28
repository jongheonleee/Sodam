package com.backend.sodam.domain.positions.entity

import com.backend.sodam.domain.positions.model.SodamPosition
import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "positions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class PositionsEntity(
    @Id
    @Column(name = "POSITION_ID")
    val positionId: String,

    // 가변 필드
    positionName: String,
    ord: Int,
    validYN: Int,
): MutableBaseEntity() {
    @Column(name = "POSITION_NAME")
    var positionName: String = positionName
        protected set

    @Column(name = "ORD")
    var ord: Int = ord
        protected set

    @Column(name = "VALID_YN")
    var validYN = validYN
        protected set

    fun toDomain(): SodamPosition {
        return SodamPosition(
            positionId = this.positionId,
            positionName = this.positionName,
            positionOrd = this.ord,
            isValid = this.validYN == 0
        )
    }
}