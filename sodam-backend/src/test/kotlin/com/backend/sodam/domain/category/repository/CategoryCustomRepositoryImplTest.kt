package com.backend.sodam.domain.category.repository


import com.backend.sodam.domain.category.entity.CategoryEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class CategoryCustomRepositoryImplTest(
    @Autowired
    val sut: CategoryCustomRepositoryImpl,
    @Autowired
    val categoryJpaRepository: CategoryJpaRepository,
) {

    private val expected : MutableList<CategoryEntity> = mutableListOf()

    @BeforeEach
    fun `테스트_데이터_등록`() {
        // 최상위 카테고리 지정
        val topCategoryId = UUID.randomUUID()

        // 총 5 가지 카테고리 데이터 등록
        for (i in 1..5) {
            val categoryEntity = CategoryEntity(
                categoryId = UUID.randomUUID(),
                topCategoryId = topCategoryId,
                categoryName = "테스트용 카테고리 $i",
                categoryOrd = i,
                validYN = 0 // 사용 가능한 카테고리는 0으로 등록됨
            )

            val savedCategoryEntity = categoryJpaRepository.save(categoryEntity)
            expected.add(savedCategoryEntity)
        }

        // validYN 0 이며 categoryOrd에 따라 정렬하기
        expected.filter   { it.validYN == 0 }
                .sortedBy { it.validYN }
    }

    @Test
    @DisplayName("사용 가능한 카테고리를 정렬 순서에 의거하여 조회한다.")
    fun `사용_가능한_카테고리를_정렬_순서에_맞게_조회한다`() {
        // 실제 조회
        val actual = sut.fetchValidCategoriesInOrder()

        // 기대값과 같은지 비교
        // - 사이즈 비교
        assertEquals(expected.size, actual.size)

        // - 내용 비교
        for (i in 0..4) {
            // 아이디, 이름, 정렬 순서
            assertEquals(expected[i].categoryId, actual[i].categoryId)
            assertEquals(expected[i].categoryName, actual[i].categoryName)
            assertEquals(expected[i].categoryOrd, actual[i].categoryOrd)
        }
    }
}