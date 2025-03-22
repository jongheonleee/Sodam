package com.backend.sodam.domain.articles.controller

import com.backend.sodam.domain.articles.service.ArticleDislikeService
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.filter.JwtTokenProvider
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class ArticleDislikeController(
    private val tokenProvider: JwtTokenProvider,
    private val articleDislikeService: ArticleDislikeService
) {

    @GetMapping("/api/v1/articles/{articleId}/dislike")
    fun dislikeArticle(@PathVariable("articleId") articleId: Long): SodamApiResponse<Unit> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(
            articleDislikeService.handleDislike(userId, articleId)
        )
    }
}
