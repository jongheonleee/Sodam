package com.backend.sodam.domain.comments.controller

import com.backend.sodam.domain.comments.service.CommentDislikeService
import com.backend.sodam.global.commons.SodamApiResponse
import com.backend.sodam.global.filter.JwtTokenProvider
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
class CommentDislikeController(
    private val tokenProvider: JwtTokenProvider,
    private val commentDislikeService: CommentDislikeService,
) {

    @GetMapping("/api/v1/comments/{commentId}/dislike")
    fun dislikeComment(
        @PathVariable("commentId") commentId: Long,
    ) : SodamApiResponse<Unit> {
        val userId = tokenProvider.getUserId()
        return SodamApiResponse.ok(
            commentDislikeService.handleDislike(commentId, userId)
        )
    }

}