package com.backend.sodam.domain.articles.controller

import com.backend.sodam.domain.articles.controller.request.ArticleCreateRequest
import com.backend.sodam.domain.articles.controller.request.ArticleSearchRequest
import com.backend.sodam.domain.articles.controller.request.ArticleUpdateRequest
import com.backend.sodam.domain.articles.service.ArticleService
import com.backend.sodam.domain.articles.service.response.ArticleCreateResponse
import com.backend.sodam.domain.articles.service.response.ArticleSummaryResponse
import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.filter.JwtTokenProvider
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
class ArticleController(
    private val articleService: ArticleService,
    private val tokenProvider: JwtTokenProvider,
) {

    // 게시글 작성
    @PostMapping("/api/v1/articles")
    fun createArticle(
        @RequestBody @Valid request: ArticleCreateRequest,
    ) : SodamApiResponse<ArticleCreateResponse> {
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
        articleSearchRequest: ArticleSearchRequest,
    ) : SodamApiResponse<Page<ArticleSummaryResponse>> {
        return SodamApiResponse.ok(
            articleService.fetchFromClient(pageable, articleSearchRequest.toCommand())
        )
    }

    // 게시글 상세 조회
    @GetMapping("/api/v1/articles/{articleId}")
    fun getArticle(@PathVariable("articleId") articleId : Long) : SodamApiResponse<String> {
        return SodamApiResponse.ok(
            "성공"
        )
    }

    // 게시글 수정
    @PutMapping("/api/v1/articles/{articleId}")
    fun putArticle(@RequestBody @Valid request : ArticleUpdateRequest, @PathVariable articleId: String) : SodamApiResponse<String> {
        return SodamApiResponse.ok(
            "성공"
        )
    }

    // 게시글 삭제
    @DeleteMapping("/api/v1/articles/{articleId}")
    fun deleteArticle(@PathVariable("articleId") articleId : Long) : SodamApiResponse<String> {
        return SodamApiResponse.ok(
            "성공"
        )
    }
}