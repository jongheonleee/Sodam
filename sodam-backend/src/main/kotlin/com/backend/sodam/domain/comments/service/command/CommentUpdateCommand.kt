package com.backend.sodam.domain.comments.service.command

data class CommentUpdateCommand(
    val content: String,
    val userId: String,
    var profileImageUrl: String = "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
)
