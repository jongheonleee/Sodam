package com.backend.sodam.domain.comments.service

import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CommentServiceIntegrationTest(
    private val sut: CommentService
) : BehaviorSpec({
})
