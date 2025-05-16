package com.backend.sodam.domain.categories.repository

import com.backend.sodam.domain.categories.model.Category
import com.backend.sodam.domain.categories.service.port.FetchCategoryPort
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class CategoryRepository(
    private val categoryJpaRepository: CategoryJpaRepository
) : FetchCategoryPort {
    // 사용가능한 카테고리를 등록된 정렬 순서로 조회한뒤, 도메인 모델로 변환하여 전달함
    @Transactional(readOnly = true)
    override fun fetchValidCategories(): List<Category> {
        val fetchedCategories = categoryJpaRepository.fetchValidCategoriesInOrder()
        return fetchedCategories.map { it.toDomain() }
    }

    @Transactional(readOnly = true)
    override fun fetchValidCategoriesByTopCategoryId(topCategoryId: String): List<Category> {
        return categoryJpaRepository.fetchValidCategoriesInOrderByTopCategoryId(topCategoryId)
    }

    @Transactional(readOnly = true)
    override fun findByCategoryId(categoryId: String): Category {
        return categoryJpaRepository.findByCategoryId(categoryId)
            .get()
            .toDomain()
    }

    @Transactional(readOnly = true)
    override fun isExistsByCategoryId(categoryId: String): Boolean =
        categoryJpaRepository.existsByCategoryId(categoryId)
}
