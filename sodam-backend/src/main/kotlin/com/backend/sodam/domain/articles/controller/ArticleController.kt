package com.backend.sodam.domain.articles.controller

import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class ArticleController {

    @GetMapping("/api/v1/articles")
    fun getArticles() : String {
        println("Get Articles")
        return "테스트"
    }
}