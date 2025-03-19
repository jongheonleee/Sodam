package com.backend.sodam.domain.comments.service.command

import com.backend.sodam.domain.articles.entity.ArticleEntity
import com.backend.sodam.domain.comments.entity.CommentEntity
import com.backend.sodam.domain.users.entity.SocialUsersEntity
import com.backend.sodam.domain.users.entity.UsersEntity

data class CommentCreateCommand(
    val content: String,
    val userId: String,
    var profileImageUrl: String = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
) {
    fun toEntity(articleEntity: ArticleEntity, userEntity: UsersEntity) : CommentEntity {
        return CommentEntity(
            userImage = profileImageUrl,
            article = articleEntity,
            user = userEntity,
            commentContent = content,
            commentLikeCnt = 0,
            commentDislikeCnt = 0
        )
    }

    fun toEntity(articleEntity: ArticleEntity, socialUsersEntity: SocialUsersEntity) : CommentEntity {
        return CommentEntity(
            userImage = profileImageUrl,
            article = articleEntity,
            socialUser = socialUsersEntity,
            commentContent = content,
            commentLikeCnt = 0,
            commentDislikeCnt = 0
        )
    }
}
