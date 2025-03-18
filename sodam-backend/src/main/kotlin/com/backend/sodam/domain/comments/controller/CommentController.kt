package com.backend.sodam.domain.comments.controller

import com.backend.sodam.domain.comments.service.CommentService
import com.backend.sodam.global.commons.SodamApiResponse
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class CommentController(
    private val commentService: CommentService,
) {

    // 특정 게시글에 댓글 생성
    @PostMapping("/api/v1/articles/{articleId}/comments")
    fun create(
        @PathVariable("articleId") articleId: Long,
    ) : SodamApiResponse<String> {
        println(articleId)
        return SodamApiResponse.ok(
            "성공"
        )
    }
}