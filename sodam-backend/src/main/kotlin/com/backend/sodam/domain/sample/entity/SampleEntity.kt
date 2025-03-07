package com.backend.sodam.domain.sample.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity
@Table(name = "sample")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SampleEntity(
    @Id
    @Column(name = "SAMPLE_ID")
    val sampleId: String,
    sampleName: String,
    sampleDescription: String
) : MutableBaseEntity() {

    @Column(name = "SAMPLE_NAME")
    var sampleName: String = sampleName
        protected set

    @Column(name = "SAMPLE_DESCRIPTION")
    var sampleDescription: String = sampleDescription
        protected set

    fun updateName(name: String) {
        this.sampleName = name
        this.modifiedAt = LocalDateTime.now()
        this.modifiedBy = "system"
    }
}
