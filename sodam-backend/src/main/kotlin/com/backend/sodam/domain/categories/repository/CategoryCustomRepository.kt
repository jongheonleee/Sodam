package com.backend.sodam.domain.categories.repository

import com.backend.sodam.domain.categories.entity.CategoryEntity

interface CategoryCustomRepository {
    // 사용가능한 카테고리를 정렬 순서대로 조회해옴
    fun fetchValidCategoriesInOrder(): List<CategoryEntity>
}
