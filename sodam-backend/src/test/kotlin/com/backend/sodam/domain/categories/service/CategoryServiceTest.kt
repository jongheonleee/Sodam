package com.backend.sodam.domain.categories.service

import com.backend.sodam.domain.categories.model.Category
import com.backend.sodam.domain.categories.service.dto.CategoriesResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*

class CategoryServiceTest : BehaviorSpec({

    // 목 활용
    val expected = mutableListOf<Category>()
    val sut = mockk<CategoryService>()

    // 테스트 환경에서 사용할 더미 데이터 설정
    beforeEach {
        val topCategoryId = UUID.randomUUID()

        for (i in 1..5) {
            val category = Category(
                categoryId = UUID.randomUUID().toString(),
                topCategoryId = topCategoryId.toString(),
                categoryName = "테스트용 카테고리 $i",
                categoryOrd = i,
                validYN = true
            )
            expected.add(category)
        }
    }

    // 테스트 진행
    given("클라이언트로부터 비즈니스에 사용하는 카테고리를 요청받았을 때") {
        `when`("총 5개의 사용 가능한 카테고리가 설정되어 있다면") {
            // 반환할 카테고리 더미 설정
            every { sut.fetchFromClient() } returns CategoriesResponse(expected)

            then("등록된 카테고리를 정렬 순서에 맞게 반환해야한다.") {
                // 실제 호출
                val actual = sut.fetchFromClient()

                // 내용 비교
                actual.categories.size shouldBe expected.size

                for (idx in 0..4) {
                    expected[idx].categoryId shouldBe expected[idx].categoryId
                    expected[idx].topCategoryId shouldBe expected[idx].topCategoryId
                    expected[idx].categoryName shouldBe expected[idx].categoryName
                }

                // 호출 검증
                verify {
                    sut.fetchFromClient()
                }
            }
        }
    }
})
