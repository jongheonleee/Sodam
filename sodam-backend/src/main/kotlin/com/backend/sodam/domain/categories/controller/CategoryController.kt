package com.backend.sodam.domain.categories.controller

import com.backend.sodam.domain.categories.service.response.CategoriesResponse
import com.backend.sodam.domain.categories.service.CategoryService
import com.backend.sodam.domain.categories.service.usecase.FetchCategoryUseCase
import com.backend.sodam.global.commons.SodamApiResponse
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class CategoryController(
    private val fetchCategoryUseCase: FetchCategoryUseCase
) {
    @GetMapping("/api/v1/categories/{topCategoryId}")
    fun getCategories(
        @PathVariable("topCategoryId") topCategoryId: String
    ): SodamApiResponse<CategoriesResponse> {
        return SodamApiResponse.ok(fetchCategoryUseCase.findCategories(topCategoryId))
    }
}
