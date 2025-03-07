package com.backend.sodam.domain.category.repository

import com.backend.sodam.domain.category.entity.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CategoryJpaRepository : JpaRepository<CategoryEntity, UUID>, CategoryCustomRepository
