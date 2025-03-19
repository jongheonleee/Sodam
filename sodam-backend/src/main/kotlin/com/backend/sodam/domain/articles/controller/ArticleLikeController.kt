package com.backend.sodam.domain.articles.controller

import com.backend.sodam.domain.articles.service.ArticleLikeService
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
    private val articleLikeService: ArticleLikeService,
) {

    @GetMapping("/api/v1/articles/{articleId}/like")
    fun likeArticle(@PathVariable("articleId") articleId: Long): SodamApiResponse<Unit> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(articleLikeService.handleLike(userId, articleId))
    }
}