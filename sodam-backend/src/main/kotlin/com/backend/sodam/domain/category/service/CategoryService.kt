package com.backend.sodam.domain.category.service

import com.backend.sodam.domain.category.repository.CategoryRepository
import com.backend.sodam.domain.category.service.dto.CategoriesResponse
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class CategoryService(
    private val categoryRepository: CategoryRepository,
) {

    // 서비스에서 운영하는 카테고리를 순서대로 조회한다.
    fun fetchFromClient() : CategoriesResponse {
        val fetchedValidCategories = categoryRepository.fetchValidCategories()
        return CategoriesResponse(fetchedValidCategories)
    }
}