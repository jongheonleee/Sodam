package com.backend.sodam.domain.categories.repository

import com.backend.sodam.domain.categories.entity.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CategoryJpaRepository : JpaRepository<CategoryEntity, UUID>, CategoryCustomRepository
