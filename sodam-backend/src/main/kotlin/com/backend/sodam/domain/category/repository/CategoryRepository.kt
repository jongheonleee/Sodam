package com.backend.sodam.domain.category.repository

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class CategoryRepository(
    private val categoryJpaRepository: CategoryJpaRepository
)
