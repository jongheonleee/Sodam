package com.backend.sodam.domain.comments.controller

import com.backend.sodam.domain.comments.controller.request.CommentCreateRequest
import com.backend.sodam.domain.comments.service.CommentService
import com.backend.sodam.domain.comments.service.response.CommentCreateResponse
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.filter.JwtTokenProvider
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class CommentController(
    private val tokenProvider: JwtTokenProvider,
    private val commentService: CommentService,
) {

    // 특정 게시글에 댓글 생성 /api/v1/articles/${articleId}/comments
    @PostMapping("/api/v1/articles/{articleId}/comments")
    fun create(
        @PathVariable("articleId") articleId: Long,
        @RequestBody @Valid commentCreateRequest: CommentCreateRequest,
    ) : SodamApiResponse<CommentCreateResponse> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(
            commentService.create(articleId, commentCreateRequest.toCommand(userId))
        )
    }
}