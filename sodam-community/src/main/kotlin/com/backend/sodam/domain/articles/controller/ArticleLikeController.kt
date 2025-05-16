package com.backend.sodam.domain.articles.controller

import com.backend.sodam.domain.articles.service.usecase.HandleArticleLikeUseCase
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.filter.JwtTokenProvider
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class ArticleLikeController(
    private val tokenProvider: JwtTokenProvider,
    private val articleLikeUseCase: HandleArticleLikeUseCase
) {

    @GetMapping("/api/v1/articles/{articleId}/like")
    fun likeArticle(@PathVariable("articleId") articleId: Long): SodamApiResponse<Unit> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(articleLikeUseCase.handleLike(userId, articleId))
    }
}
