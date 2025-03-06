package com.backend.sodam.global.audit

import com.backend.sodam.domain.sample.entity.SampleEntity
import com.backend.sodam.domain.sample.repository.SampleJpaRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuditTest(
    val sampleJpaRepository: SampleJpaRepository
) : FunSpec({

    test("1. 샘플 생성 시 createdAt과 modifiedAt이 생성된 시각으로 저장되야 한다.") {
        val sampleEntity = SampleEntity(
            sampleId = "SP001",
            sampleName = "샘플 테스트",
            sampleDescription = "샘플 테스트"
        )

        val savedSampleEntity = sampleJpaRepository.save(sampleEntity)

        savedSampleEntity.createdAt shouldBe sampleEntity.modifiedAt
    }

    test("2. 샘플을 수정하면 modifiedtAt은 변경된 시각으로 수정되어야 한다. ") {
        // 샘플 한개 등록함
        val sampleEntity = SampleEntity(
            sampleId = "SP001",
            sampleName = "샘플 테스트",
            sampleDescription = "샘플 테스트"
        )
        val savedSampleEntity = sampleJpaRepository.save(sampleEntity)

        // 스레드 1초 대기
        Thread.sleep(1000)

        // 영속성 대상인 샘플의 필드값을 변경함
        val foundSampleEntity = sampleJpaRepository.findById(savedSampleEntity.sampleId).get()
        foundSampleEntity.updateName("수정된 이름")

        // 변경된 대상의 생성 시각과 수정 시각이 서로 달라야한다.
        foundSampleEntity.modifiedAt shouldNotBe foundSampleEntity.createdAt
        foundSampleEntity.sampleName shouldBe "수정된 이름"
    }
})
