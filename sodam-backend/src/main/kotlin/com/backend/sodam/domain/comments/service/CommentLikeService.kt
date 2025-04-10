package com.backend.sodam.domain.comments.service

import com.backend.sodam.domain.comments.exception.CommentException
import com.backend.sodam.domain.comments.service.port.CreateUserCommentLikePort
import com.backend.sodam.domain.comments.service.port.DeleteUserCommentLikePort
import com.backend.sodam.domain.comments.service.port.FetchCommentPort
import com.backend.sodam.domain.comments.service.port.FetchUserCommentLikePort
import com.backend.sodam.domain.comments.service.port.UpdateCommentPort
import com.backend.sodam.domain.comments.service.usecase.HandleCommentLikeUseCase
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.port.FetchUserPort
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class CommentLikeService(
    private val fetchUserPorts: List<FetchUserPort>,
    private val createCommentLikePorts: List<CreateUserCommentLikePort>,
    private val fetchCommentLikePorts: List<FetchUserCommentLikePort>,
    private val deleteCommentLikePorts: List<DeleteUserCommentLikePort>,
    private val updateCommentPorts: List<UpdateCommentPort>,
    private val fetchCommentPorts: List<FetchCommentPort>,
): HandleCommentLikeUseCase {

    @Transactional
    override fun handleLike(commentId: Long, userId: String) {
        checkCommentExists(commentId = commentId)
        val userType = extractUserType(userId = userId)
        val fetchCommentLikePort = getFetchCommentLikePort(userType)
        val updateCommentPort = getUpdateCommentPort()
        val isExists = fetchCommentLikePort.existsCommentLike(commentId = commentId, userId = userId)
        when(isExists) {
            true -> {
                val deleteCommentLikePort = getDeleteCommentLikePort(userType)
                deleteCommentLikePort.deleteLike(commentId = commentId, userId = userId)
                updateCommentPort.decreaseLikeCnt(commentId = commentId)
            }
            false -> {
                val createCommentLikePort = getCreateCommentLikePort(userType)
                createCommentLikePort.createLike(commentId = commentId, userId = userId)
                updateCommentPort.increaseLikeCnt(commentId = commentId)
            }
        }
    }

    // 📌 작업 유효성을 검증하는 메서드
    private fun checkCommentExists(commentId: Long) {
        if ( ! isExistsComment(commentId) )
            throw CommentException.CommentNotFoundException()

    }

    private fun isExistsComment(commentId: Long): Boolean = getFetchCommentPort().isExistsComment(commentId = commentId)

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

    private fun getCreateCommentLikePort(userType: UserType): CreateUserCommentLikePort =
        createCommentLikePorts.stream()
            .filter{ it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getFetchCommentLikePort(userType: UserType): FetchUserCommentLikePort =
        fetchCommentLikePorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getDeleteCommentLikePort(userType: UserType): DeleteUserCommentLikePort =
        deleteCommentLikePorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getUpdateCommentPort(): UpdateCommentPort =
        updateCommentPorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getFetchCommentPort(): FetchCommentPort =
        fetchCommentPorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }
}
