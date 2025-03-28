package com.backend.sodam.domain.categories.model

import java.util.*

// 도메인 모델
class Category(
    val categoryId: String,
    val topCategoryId: String,
    val categoryName: String,
    private var categoryOrd: Int,
    private var validYN: Boolean
) {

    fun invalidate() {
        this.validYN = false
    }

    fun validate() {
        this.validYN = true
    }

    fun isValid(): Boolean {
        return this.validYN
    }

    companion object {
        fun newCategory(topCategoryId: String, categoryName: String, categoryOrd: Int): Category {
            return Category(
                categoryId = UUID.randomUUID().toString(),
                topCategoryId = topCategoryId,
                categoryName = categoryName,
                categoryOrd = categoryOrd,
                validYN = true // 기본적으로 사용 가능 하게 만듦
            )
        }
    }
}
