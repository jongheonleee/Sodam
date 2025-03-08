package com.backend.sodam.domain.categories.controller

import com.backend.sodam.domain.categories.service.CategoryService
import com.backend.sodam.domain.categories.service.dto.CategoriesResponse
import com.backend.sodam.global.commons.SodamApiResponse
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class CategoryController(
    private val categoryService: CategoryService
) {
    @GetMapping("/api/v1/categories")
    fun getCategories(): SodamApiResponse<CategoriesResponse> {
        val fetchedCategories = categoryService.fetchFromClient()
        return SodamApiResponse.ok(fetchedCategories)
    }
}
