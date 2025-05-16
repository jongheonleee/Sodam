package com.backend.sodam.domain.policy.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "sanctions_policy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SanctionsPolicyEntity(
    @Id
    @Column(name = "POLICY_ID")
    val policyId: String,

    @OneToOne
    @JoinColumn(name = "RULE_ID1")
    var firstRule: RulesEntity? = null,

    @Column(name = "OPERATOR1")
    val operator1: String,

    @OneToOne
    @JoinColumn(name = "RULE_ID2")
    var secondRule: RulesEntity? = null,

    @Column(name = "OPERATOR2")
    val operator2: String,

    @OneToOne
    @JoinColumn(name = "RULE_ID3")
    var thirdRule: RulesEntity? = null,

    validYN: Int

) : MutableBaseEntity() {

    @Column(name = "VALID_YN")
    var validYN = validYN
        protected set
}
