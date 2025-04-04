package com.backend.sodam.domain.comments.service

import com.backend.sodam.domain.comments.repository.CommentRepository
import com.backend.sodam.domain.comments.repository.UsersCommentDislikeRepository
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserRepository
import com.backend.sodam.domain.users.service.port.FetchUserPort
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class CommentDislikeService(
    private val fetchUserPorts: List<FetchUserPort>,
    private val commentRepository: CommentRepository,
    private val usersDislikeCommentRepository: UsersCommentDislikeRepository
) {
    fun handleDislike(commentId: Long, userId: String) {
        // [비즈니스 로직]
        // 유저 정보를 조회한다
        // 유저 타입에 따라서 다르게 서로 다르게 적용한다.
        val userType = extractUserType(userId = userId)
        // 유저 정보를 조회한다
        // 유저 타입에 따라서 다르게 서로 다르게 적용한다.
        // 좋아요 핸들링 처리를 진행한다.
        // 기존에 눌렀다면, 해당 회원 좋아요 댓글 로우를 삭제한다.
        // 해당 댓글의 좋아요 수를 -1 한다.

        // 처음 눌렀거나 좋아요 눌렀던 기록이 없다면, 해당 회원 좋아요 댓글 로우를 생성한다.
        // 해당 댓글의 좋아요 수를 +1 한다.

        val isExists = when (userType) {
            UserType.SOCIAL -> {
                usersDislikeCommentRepository.existsByCommentDislikeForSocialUser(
                    commentId = commentId,
                    socialUserId = userId
                )
            }

            else -> {
                usersDislikeCommentRepository.existsByCommentDislikeForUser(
                    commentId = commentId,
                    userId = userId
                )
            }
        }

        if (isExists) {
            when (userType) {
                UserType.SOCIAL -> {
                    usersDislikeCommentRepository.deleteForSocialUser(
                        commentId = commentId,
                        socialUserId = userId
                    )
                }

                else -> {
                    usersDislikeCommentRepository.deleteForUser(
                        commentId = commentId,
                        userId = userId
                    )
                }
            }

            commentRepository.decreaseDislikeCnt(commentId)
        } else {
            when (userType) {
                UserType.SOCIAL -> {
                    usersDislikeCommentRepository.createForSocialUser(
                        commentId = commentId,
                        socialUserId = userId
                    )
                }

                else -> {
                    usersDislikeCommentRepository.createForUser(
                        commentId = commentId,
                        userId = userId
                    )
                }
            }

            commentRepository.increaseDislikeCnt(commentId)
        }
    }

    // 📌 특정 유저의 부가정보를 조회하는 추출 메서드
    private fun extractUserType(userId: String): UserType {
        val fetchPort = getFetchPortByUserId(userId)
        val sodamUser = fetchPort.findByUserId(userId).get()
        return sodamUser.userType
    }

    // 📌 특정 조건에 부합한 포트 조회용 메서드 - 런타임 시점에 특정 비즈니스 로직을 처리할 수 있는 빈을 선택하는 메서드
    private fun getFetchPortByUserType(userType: UserType): FetchUserPort =
        fetchUserPorts.stream()
            .filter { it.isTarget(userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getFetchPortByUserId(userId: String): FetchUserPort =
        fetchUserPorts.stream()
            .filter { it.isExistsByUserId(userId) }
            .findFirst()
            .orElseThrow { UserException.UserNotFoundException() }
}
