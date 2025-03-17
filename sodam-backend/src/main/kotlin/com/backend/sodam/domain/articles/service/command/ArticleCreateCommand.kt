package com.backend.sodam.domain.articles.service.command

import com.backend.sodam.domain.articles.entity.ArticleEntity
import com.backend.sodam.domain.categories.entity.CategoryEntity
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.entity.UsersEntity


data class ArticleCreateCommand(
    val categoryId: String,
    val title: String,
    val summary: String,
    val content: String,
    val tags: List<String>,
) {

    fun toEntity(socialUsersEntity: SocialUsersEntity, categoryEntity: CategoryEntity) : ArticleEntity {
        return ArticleEntity(
            name = socialUsersEntity.userName,
            category = categoryEntity,
            socialUser = socialUsersEntity,
            articleTitle = title,
            articleSummary = summary,
            articleContent = content,
            articleViewCnt = 0,
            articleLikeCnt = 0,
            articleDislikeCnt = 0
        )
    }

    fun toEntity(userEntity: UsersEntity, categoryEntity: CategoryEntity) : ArticleEntity {
        return ArticleEntity(
            name = userEntity.userName,
            category = categoryEntity,
            user = userEntity,
            articleTitle = title,
            articleSummary = summary,
            articleContent = content,
            articleViewCnt = 0,
            articleLikeCnt = 0,
            articleDislikeCnt = 0
        )
    }
}
