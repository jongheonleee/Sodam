package com.backend.sodam.domain.articles.controller

import com.backend.sodam.domain.articles.controller.request.ArticleCreateRequest
import com.backend.sodam.domain.articles.controller.request.ArticleSearchRequest
import com.backend.sodam.domain.articles.controller.request.ArticleUpdateRequest
import com.backend.sodam.domain.articles.service.ArticleService
import com.backend.sodam.domain.articles.service.response.*
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.filter.JwtTokenProvider
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class ArticleController(
    private val articleService: ArticleService,
    private val tokenProvider: JwtTokenProvider
) {

    // 게시글 작성
    @PostMapping("/api/v1/articles")
    fun createArticle(
        @RequestBody @Valid
        request: ArticleCreateRequest
    ): SodamApiResponse<ArticleCreateResponse> {
        val userId = tokenProvider.getUserId()
        val command = request.toCommand()
        return SodamApiResponse.ok(
            articleService.create(userId, command)
        )
    }

    // 게시글 여러개 조회
    @GetMapping("/api/v1/articles")
    fun getArticles(
        pageable: Pageable,
        articleSearchRequest: ArticleSearchRequest
    ): SodamApiResponse<Page<ArticleSummaryResponse>> {
        println(articleSearchRequest.toString())
        val command = articleSearchRequest.toCommand()
        return SodamApiResponse.ok(
            articleService.fetchFromClient(pageable, command)
        )
    }

    // 게시글 상세 조회
    @GetMapping("/api/v1/articles/{articleId}")
    fun getArticle(@PathVariable("articleId") articleId: Long): SodamApiResponse<ArticleDetailResponse> {
        return SodamApiResponse.ok(
            articleService.getArticleDetail(articleId)
        )
    }

    // 게시글 단순 조회 - 게시글 수정 처리용
    @GetMapping("/api/v1/articles/simple/{articleId}")
    fun getArticleSimple(
        @PathVariable("articleId") articleId: Long
    ): SodamApiResponse<ArticleSimpleResponse> {
        return SodamApiResponse.ok(
            articleService.getArticleSimple(articleId)
        )
    }

    // 게시글 수정
    @PutMapping("/api/v1/articles/{articleId}")
    fun putArticle(
        @PathVariable("articleId") articleId: Long,
        @RequestBody @Valid
        request: ArticleUpdateRequest
    ): SodamApiResponse<ArticleUpdateResponse> {
        val userId = tokenProvider.getUserId()
        val command = request.toCommand(userId)
        return SodamApiResponse.ok(
            articleService.update(articleId, command)
        )
    }

    // 게시글 삭제
    @DeleteMapping("/api/v1/articles/{articleId}")
    fun deleteArticle(@PathVariable("articleId") articleId: Long): SodamApiResponse<Unit> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(
            articleService.delete(userId, articleId)
        )
    }
}
