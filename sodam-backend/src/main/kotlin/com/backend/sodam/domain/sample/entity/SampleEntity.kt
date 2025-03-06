package com.backend.sodam.domain.article.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor

@Getter
@Entity
@Table(name = "sample")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SampleEntity (
    @Id
    @Column(name = "SAMPLE_ID")
    val sampleId: String,

    @Column(name = "SAMPLE_NAME")
    val sampleName: String,

    @Column(name = "SAMPLE_DESC")
    val sampleDescription: String
) {

}
