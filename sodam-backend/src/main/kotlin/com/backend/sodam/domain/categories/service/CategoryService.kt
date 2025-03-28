package com.backend.sodam.domain.categories.service

import com.backend.sodam.domain.categories.repository.CategoryRepository
import com.backend.sodam.domain.categories.service.response.CategoriesResponse
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    // 서비스에서 운영하는 카테고리를 순서대로 조회한다.
    fun fetchFromClient(): CategoriesResponse {
        val fetchedValidCategories = categoryRepository.fetchValidCategories()
        return CategoriesResponse(fetchedValidCategories)
    }

    fun findCategories(topCategoryId: String): CategoriesResponse {
        val fetchedValidCategories = categoryRepository.fetchValidCategoriesByTopCategoryId(
            topCategoryId = topCategoryId
        )
        return CategoriesResponse(fetchedValidCategories)
    }
}
