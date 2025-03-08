package com.backend.sodam.domain.categories.service.dto

import com.backend.sodam.domain.categories.model.Category

data class CategoriesResponse(
    val categories: List<Category>
)
