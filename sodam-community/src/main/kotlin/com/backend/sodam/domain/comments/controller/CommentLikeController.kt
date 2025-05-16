package com.backend.sodam.domain.comments.controller

import com.backend.sodam.domain.comments.service.usecase.HandleCommentLikeUseCase
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.filter.JwtTokenProvider
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class CommentLikeController(
    private val tokenProvider: JwtTokenProvider,
    private val commentLikeUseCase: HandleCommentLikeUseCase
) {

    @GetMapping("/api/v1/comments/{commentId}/like")
    fun likeComment(
        @PathVariable("commentId") commentId: Long
    ): SodamApiResponse<Unit> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(commentLikeUseCase.handleLike(commentId, userId))
    }
}
