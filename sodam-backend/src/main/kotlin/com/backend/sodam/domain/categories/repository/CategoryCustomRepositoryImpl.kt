package com.backend.sodam.domain.categories.repository

import com.backend.sodam.domain.categories.entity.CategoryEntity
import com.backend.sodam.domain.categories.entity.QCategoryEntity.categoryEntity
import com.backend.sodam.domain.categories.model.Category
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class CategoryCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CategoryCustomRepository {

    // 사용 가능한 카테고리를 등록된 정렬 순서에 따라 조회한다.
    @Transactional(readOnly = true)
    override fun fetchValidCategoriesInOrder(): List<CategoryEntity> {
        return jpaQueryFactory.selectFrom(categoryEntity)
            .where(categoryEntity.validYN.eq(0)) // 사용 가능한 카테고리는 0으로 등록되어 있음
            .orderBy(categoryEntity.categoryOrd.asc())
            .fetch()
            .toList()
    }

    @Transactional(readOnly = true)
    override fun fetchValidCategoriesInOrderByTopCategoryId(topCategoryId: String): List<Category> {
        return jpaQueryFactory.selectFrom(categoryEntity)
                              .where(categoryEntity.topCategoryId.eq(topCategoryId)
                                  .and(categoryEntity.validYN.eq(0)))
                              .orderBy(categoryEntity.categoryOrd.asc())
                              .fetch()
                              .map { it.toDomain() }
    }
}
