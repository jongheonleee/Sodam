package com.backend.sodam.domain.category.service.dto

import com.backend.sodam.domain.category.model.Category

data class CategoriesResponse(
    val categories: List<Category>
)
