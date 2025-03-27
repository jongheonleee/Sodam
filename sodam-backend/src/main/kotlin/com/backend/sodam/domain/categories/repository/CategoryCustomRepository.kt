package com.backend.sodam.domain.categories.repository

import com.backend.sodam.domain.categories.entity.CategoryEntity
import com.backend.sodam.domain.categories.model.Category

interface CategoryCustomRepository {
    fun fetchValidCategoriesInOrder(): List<CategoryEntity>
    fun fetchValidCategoriesInOrderByTopCategoryId(topCategoryId: String): List<Category>
}
