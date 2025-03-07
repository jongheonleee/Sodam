package com.backend.sodam.domain.category.controller

import com.backend.sodam.domain.category.service.CategoryService
import com.backend.sodam.domain.category.service.dto.CategoriesResponse
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
