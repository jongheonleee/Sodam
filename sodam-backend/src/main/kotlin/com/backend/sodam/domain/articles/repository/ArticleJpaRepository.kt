package com.backend.sodam.domain.articles.repository

import com.backend.sodam.domain.articles.entity.ArticleEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ArticleJpaRepository : JpaRepository<ArticleEntity, Long>, ArticleCustomRepository {
    fun findByArticleId(articleId: Long): Optional<ArticleEntity>
}