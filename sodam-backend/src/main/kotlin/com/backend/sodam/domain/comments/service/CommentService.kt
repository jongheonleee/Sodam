package com.backend.sodam.domain.comments.service

import com.backend.sodam.domain.articles.exception.ArticleException
import com.backend.sodam.domain.articles.repository.ArticleRepositoryForNormalUser
import com.backend.sodam.domain.articles.service.port.FetchArticlePort
import com.backend.sodam.domain.comments.controller.response.CommentCreateResponse
import com.backend.sodam.domain.comments.controller.response.CommentSimpleResponse
import com.backend.sodam.domain.comments.controller.response.CommentUpdateResponse
import com.backend.sodam.domain.comments.exception.CommentException
import com.backend.sodam.domain.comments.repository.CommentRepositoryForNormalUser
import com.backend.sodam.domain.comments.service.command.CommentCreateCommand
import com.backend.sodam.domain.comments.service.command.CommentUpdateCommand
import com.backend.sodam.domain.comments.service.port.CreateCommentPort
import com.backend.sodam.domain.comments.service.port.DeleteCommentPort
import com.backend.sodam.domain.comments.service.port.FetchCommentPort
import com.backend.sodam.domain.comments.service.port.UpdateCommentPort
import com.backend.sodam.domain.comments.service.usecase.CreateCommentUseCase
import com.backend.sodam.domain.comments.service.usecase.DeleteCommentUseCase
import com.backend.sodam.domain.comments.service.usecase.FetchCommentUseCase
import com.backend.sodam.domain.comments.service.usecase.UpdateCommentUseCase
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserRepository
import com.backend.sodam.domain.users.service.port.FetchUserPort
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class CommentService(
    // 회원
    private val fetchUserPorts: List<FetchUserPort>,
    // 게시글
    private val fetchArticlePorts: List<FetchArticlePort>,
    // 댓글
    private val createCommentPorts: List<CreateCommentPort>,
    private val fetchCommentPorts: List<FetchCommentPort>,
    private val updateCommentPorts: List<UpdateCommentPort>,
    private val deleteCommentPorts: List<DeleteCommentPort>,
): CreateCommentUseCase, FetchCommentUseCase, UpdateCommentUseCase, DeleteCommentUseCase {

    override fun create(articleId: Long, commentCreateCommand: CommentCreateCommand): CommentCreateResponse {
        checkExistsArticle(articleId = articleId)
        val userType = extractUserType(commentCreateCommand.userId)
        val createCommentPort = getCreateCommentPort(userType)
        return createCommentPort.createComment(articleId = articleId, commentCreateCommand = commentCreateCommand)
                                .toCreateResponse()
    }

    override fun update(commentId: Long, commentUpdateCommand: CommentUpdateCommand): CommentUpdateResponse {
        checkExistsComment(commentId = commentId)
        val fetchCommentPort = getFetchCommentPort()
        val updateCommentPort = getUpdateCommentPort()
        val sodamComment = fetchCommentPort.findByCommentId(commentId)
        if ( ! sodamComment.canAccess(commentUpdateCommand.userId) )
            throw CommentException.CommentAccessDeniedException()
        return updateCommentPort.update(commentId, commentUpdateCommand)
                                .toUpdateResponse()
    }

    override fun getSimpleComment(commentId: Long): CommentSimpleResponse {
        checkExistsComment(commentId = commentId)
        val fetchCommentPort = getFetchCommentPort()
        return fetchCommentPort.findByCommentId(commentId = commentId)
                               .toSimpleResponse()
    }

    override fun delete(userId: String, commentId: Long) {
        checkExistsComment(commentId = commentId)
        val fetchCommentPort = getFetchCommentPort()
        val deleteCommentPort = getDeleteCommentPort()
        val sodamComment = fetchCommentPort.findByCommentId(commentId)
        if ( ! sodamComment.canAccess(userId) )
            throw CommentException.CommentAccessDeniedException()
        deleteCommentPort.delete(commentId)
    }

    // 📌 비즈니스 로직 적용 전 작업 유효성 따지는 메서드
    private fun checkExistsArticle(articleId: Long) {
        if ( ! isExistsArticle(articleId) )
            throw ArticleException.ArticleNotFoundException()
    }

    private fun checkExistsComment(commentId: Long) {
        if ( ! isExistsComment(commentId) )
            throw CommentException.CommentNotFoundException()
    }

    private fun isExistsArticle(articleId: Long): Boolean =
        fetchArticlePorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }
            .isExistsByArticleId(articleId)

    private fun isExistsComment(commentId: Long): Boolean =
        fetchCommentPorts.stream()
            .anyMatch { it.isExistsComment(commentId = commentId) }

    // 📌 특정 유저의 부가정보를 조회하는 추출 메서드
    private fun extractUserType(userId: String): UserType {
        val fetchPort = getFetchPortByUserId(userId)
        val sodamUser = fetchPort.findByUserId(userId).get()
        return sodamUser.userType
    }

    // 📌 특정 조건에 부합한 포트 조회용 메서드 - 런타임 시점에 특정 비즈니스 로직을 처리할 수 있는 빈을 선택하는 메서드
    private fun getFetchPortByUserId(userId: String): FetchUserPort =
        fetchUserPorts.stream()
            .filter { it.isExistsByUserId(userId) }
            .findFirst()
            .orElseThrow { UserException.UserNotFoundException() }

    private fun getCreateCommentPort(userType: UserType): CreateCommentPort =
        createCommentPorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getFetchCommentPort(): FetchCommentPort =
        fetchCommentPorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getUpdateCommentPort(): UpdateCommentPort =
        updateCommentPorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getDeleteCommentPort(): DeleteCommentPort =
        deleteCommentPorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }
}
