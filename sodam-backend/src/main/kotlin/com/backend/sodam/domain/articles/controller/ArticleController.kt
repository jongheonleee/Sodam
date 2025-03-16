package com.backend.sodam.domain.articles.controller

import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class ArticleController {

    // 요청 보낼시 토큰 전달하는 거 확인
    // 하지만, 내부적으로 role 처리가 제대로 안되 있어서 예외가 발생함
    // -
    @GetMapping("/api/v1/articles")
    fun getArticles() : String {
        println("Get Articles")
        return "테스트"
    }
}