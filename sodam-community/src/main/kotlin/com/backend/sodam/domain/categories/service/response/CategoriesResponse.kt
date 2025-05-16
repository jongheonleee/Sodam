package com.backend.sodam.domain.categories.service.response

import com.backend.sodam.domain.categories.model.Category

data class CategoriesResponse(
    val categories: List<Category>
)
