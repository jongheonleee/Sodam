package com.backend.sodam.domain.categories.service.port

import com.backend.sodam.domain.categories.model.Category

interface FetchCategoryPort {
    fun fetchValidCategories(): List<Category>
    fun fetchValidCategoriesByTopCategoryId(topCategoryId: String): List<Category>
    fun findByCategoryId(categoryId: String): Category
    fun isExistsByCategoryId(categoryId: String): Boolean
}
