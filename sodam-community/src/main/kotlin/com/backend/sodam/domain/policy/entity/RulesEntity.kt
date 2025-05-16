package com.backend.sodam.domain.policy.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "rules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class RulesEntity(
    @Id
    @Column(name = "RULE_ID")
    val ruleId: String,

    @Column(name = "RULE_KIND")
    val ruleKind: Int, // 쿠폰 정책에서 사용하는지, 제제 정책에서 사용하는지 구분 -> 추후에 Enum으로 변경 예정
    @Column(name = "TARGET_INDEX")
    val targetIndex: Int, // 회원 엔티티에서 혐오발언, 장애인 비하 발언, ... index로 매핑할 예정 -> 추후에 Enum으로 예정
    @Column(name = "TARGET_OPERATOR")
    val targetOperator: String, // 조건 표현식에 사용되는 연산자, target <= value 에서 <=을 의미

    // 가변 필드
    targetValue: Int, // 가변필드 비교대상의 기준값
    validYN: Int // 가변필드 사용가능 여부
) : MutableBaseEntity() {

    @Column(name = "TARGET_VALUE")
    var targetValue: Int = targetValue
        protected set

    @Column(name = "VALID_YN")
    var validYN: Int = validYN
        protected set
}
