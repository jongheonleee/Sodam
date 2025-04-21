package com.backend.sodam.domain.categories.service

import com.backend.sodam.domain.categories.service.response.CategoriesResponse
import com.backend.sodam.domain.categories.repository.CategoryRepository
import com.backend.sodam.domain.categories.service.port.FetchCategoryPort
import com.backend.sodam.domain.categories.service.usecase.FetchCategoryUseCase
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class CategoryService(
    private val fetchCategoryPort: FetchCategoryPort
): FetchCategoryUseCase {
    override fun fetchFromClient(): CategoriesResponse {
        val fetchedValidCategories = fetchCategoryPort.fetchValidCategories()
        return CategoriesResponse(fetchedValidCategories)
    }

    override fun findCategories(topCategoryId: String): CategoriesResponse {
        val fetchedValidCategories = fetchCategoryPort.fetchValidCategoriesByTopCategoryId(topCategoryId = topCategoryId)
        return CategoriesResponse(fetchedValidCategories)
    }
}
