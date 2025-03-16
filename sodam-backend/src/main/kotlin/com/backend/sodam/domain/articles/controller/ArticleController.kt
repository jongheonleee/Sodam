package com.backend.sodam.domain.articles.controller

import com.backend.sodam.domain.articles.controller.request.ArticleCreateRequest
import com.backend.sodam.global.commons.SodamApiResponse
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class ArticleController {

    @PostMapping("/api/v1/articles")
    fun getArticles(@RequestBody @Valid request: ArticleCreateRequest) : SodamApiResponse<String> {

        return SodamApiResponse.ok(
            "성공"
        )
    }
}