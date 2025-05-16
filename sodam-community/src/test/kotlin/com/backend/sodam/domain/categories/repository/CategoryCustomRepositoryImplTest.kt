package com.backend.sodam.domain.categories.repository

import com.backend.sodam.domain.categories.entity.CategoryEntity
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class CategoryCustomRepositoryImplTest(
    private val sut: CategoryCustomRepositoryImpl,
    private val categoryJpaRepository: CategoryJpaRepository
) : DescribeSpec({

    val expected: MutableList<CategoryEntity> = mutableListOf()

    // 테스트 환경 구축
    beforeEach {
        // 최상위 카테고리 지정
        val topCategoryId = UUID.randomUUID().toString()

        // 총 5 가지 카테고리 데이터 등록
        for (i in 1..5) {
            val categoryEntity = CategoryEntity(
                categoryId = UUID.randomUUID().toString(),
                topCategoryId = topCategoryId,
                categoryName = "테스트용 카테고리 $i",
                categoryOrd = i,
                validYN = 0 // 사용 가능한 카테고리는 0으로 등록됨
            )

            val savedCategoryEntity = categoryJpaRepository.save(categoryEntity)
            expected.add(savedCategoryEntity)
        }

        // validYN 0 이며 categoryOrd에 따라 정렬하기
        expected.filter { it.validYN == 0 }
            .sortedBy { it.validYN }
    }

    describe("카테고리 조회 테스트") {
        context("여러 카테고리 조회 요청을 받으면") {
            it("사용 가능한 카테고리를 순서대로 조회한다") {
                // 실제 조회
                val actual = sut.fetchValidCategoriesInOrder()

                // 기대값과 같은지 비교
                // - 사이즈 비교
                expected.size shouldBe actual.size

                // - 내용 비교
                for (idx in 0..4) {
                    // 아이디, 이름, 정렬 순서
                    expected[idx].categoryId shouldBe actual[idx].categoryId
                    expected[idx].categoryName shouldBe actual[idx].categoryName
                    expected[idx].categoryOrd shouldBe actual[idx].categoryOrd
                }
            }
        }
    }
})
