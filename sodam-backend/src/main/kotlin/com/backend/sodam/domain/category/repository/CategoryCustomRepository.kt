package com.backend.sodam.domain.category.repository

import com.backend.sodam.domain.category.entity.CategoryEntity

interface CategoryCustomRepository {
    // 사용가능한 카테고리를 정렬 순서대로 조회해옴
    fun fetchValidCategoriesInOrder(): List<CategoryEntity>
}
