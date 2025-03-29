package com.backend.sodam.domain.comments.controller

import com.backend.sodam.domain.comments.controller.request.CommentCreateRequest
import com.backend.sodam.domain.comments.controller.request.CommentUpdateRequest
import com.backend.sodam.domain.comments.service.CommentService
import com.backend.sodam.domain.comments.controller.response.CommentCreateResponse
import com.backend.sodam.domain.comments.controller.response.CommentSimpleResponse
import com.backend.sodam.domain.comments.controller.response.CommentUpdateResponse
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.filter.JwtTokenProvider
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class CommentController(
    private val tokenProvider: JwtTokenProvider,
    private val commentService: CommentService
) {

    // 댓글 작성
    @PostMapping("/api/v1/articles/{articleId}/comments")
    fun create(
        @PathVariable("articleId") articleId: Long,
        @RequestBody @Valid
        commentCreateRequest: CommentCreateRequest
    ): SodamApiResponse<CommentCreateResponse> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(
            commentService.create(articleId, commentCreateRequest.toCommand(userId))
        )
    }

    // 특정 댓글 단순 조회 - 클라이언트용
    @GetMapping("/api/v1/comments/{commentId}")
    fun getComment(
        @PathVariable("commentId") commentId: Long
    ): SodamApiResponse<CommentSimpleResponse> {
        return SodamApiResponse.ok(
            commentService.getSimpleComment(commentId)
        )
    }

    // 댓글 수정
    @PutMapping("/api/v1/comments/{commentId}")
    fun update(
        @PathVariable("commentId") commentId: Long,
        @RequestBody @Valid
        commentUpdateRequest: CommentUpdateRequest
    ): SodamApiResponse<CommentUpdateResponse> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(
            commentService.update(commentId, commentUpdateRequest.toCommand(userId))
        )
    }

    // 댓글 삭제
    @DeleteMapping("/api/v1/comments/{commentId}")
    fun delete(
        @PathVariable("commentId") commentId: Long
    ): SodamApiResponse<Unit> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(
            commentService.delete(userId, commentId)
        )
    }
}
