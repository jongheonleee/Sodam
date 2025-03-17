package com.backend.sodam.domain.articles.service

import com.backend.sodam.domain.articles.repository.ArticleRepository
import com.backend.sodam.domain.articles.service.command.ArticleCreateCommand
import com.backend.sodam.domain.articles.service.response.ArticleCreateResponse
import com.backend.sodam.domain.categories.repository.CategoryRepository
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.SocialUserRepository
import com.backend.sodam.domain.users.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ArticleService(
    private val userRepository: UserRepository,
    private val socialUserRepository: SocialUserRepository,
    private val articleRepository: ArticleRepository,
) {

    fun create(userId: String, articleCreateCommand: ArticleCreateCommand) : ArticleCreateResponse {
        val sodamUser = socialUserRepository.findBySocialUserId(userId)
                .orElseGet { userRepository.findByUserId(userId)
                .orElseThrow { UserException.UserNotFoundException() } }


        val sodamArticle = when (sodamUser.userType) {
            UserType.SOCIAL -> {
                articleRepository.createArticleForSocialUser(
                    userId,
                    articleCreateCommand,
                )
            }
            else -> {
                articleRepository.createArticleForUser(
                    userId,
                    articleCreateCommand,
                )
            }
        }

        return ArticleCreateResponse(
            articleId = sodamArticle.articleId,
            title = articleCreateCommand.title,
            author = sodamArticle.author,
            summary = sodamArticle.summary,
            content = sodamArticle.content,
            tags = sodamArticle.tags,
        )
    }

}