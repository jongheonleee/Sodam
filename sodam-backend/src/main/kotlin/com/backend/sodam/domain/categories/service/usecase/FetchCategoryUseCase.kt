package com.backend.sodam.domain.categories.service.usecase

import com.backend.sodam.domain.categories.controller.response.CategoriesResponse

interface FetchCategoryUseCase {
    fun fetchFromClient(): CategoriesResponse
    fun findCategories(topCategoryId: String): CategoriesResponse
}