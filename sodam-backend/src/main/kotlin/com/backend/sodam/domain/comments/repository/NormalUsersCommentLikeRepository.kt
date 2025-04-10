package com.backend.sodam.domain.comments.repository

import com.backend.sodam.domain.comments.entity.UsersLikeCommentEntity
import com.backend.sodam.domain.comments.exception.CommentException
import com.backend.sodam.domain.comments.service.port.CreateUserCommentLikePort
import com.backend.sodam.domain.comments.service.port.DeleteUserCommentLikePort
import com.backend.sodam.domain.comments.service.port.FetchUserCommentLikePort
import com.backend.sodam.domain.comments.service.port.UpdateUserCommentLikePort
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import com.backend.sodam.domain.users.repository.SocialUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class NormalUsersCommentLikeRepository(
    private val commentJpaRepository: CommentJpaRepository,
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val usersCommentLikeJpaRepository: UsersCommentLikeJpaRepository
): CreateUserCommentLikePort, FetchUserCommentLikePort, UpdateUserCommentLikePort, DeleteUserCommentLikePort {

    override fun isTarget(userType: UserType): Boolean =
        UserType.NORMAL == userType

    @Transactional
    override fun createLike(commentId: Long, userId: String) {
        val userEntity = normalUserJpaRepository.findByUserId(userId).get()
        val commentEntity = commentJpaRepository.findByCommentId(commentId).get()
        val commentLikeEntity = UsersLikeCommentEntity(
            user = userEntity,
            comment = commentEntity
        )
        usersCommentLikeJpaRepository.save(commentLikeEntity)
    }

    @Transactional(readOnly = true)
    override fun existsCommentLike(commentId: Long, userId: String): Boolean {
        val userEntity = normalUserJpaRepository.findByUserId(userId).get()
        val commentEntity = commentJpaRepository.findByCommentId(commentId).get()
        return usersCommentLikeJpaRepository.existsByCommentAndUser(
            user = userEntity,
            comment = commentEntity,
        )
    }

    @Transactional
    override fun deleteLike(commentId: Long, userId: String) {
        val userEntity = normalUserJpaRepository.findByUserId(userId = userId).get()
        val commentEntity = commentJpaRepository.findByCommentId(commentId = commentId).get()
        val commentLikeEntity = usersCommentLikeJpaRepository.findByCommentAndUser(
            user = userEntity,
            comment = commentEntity
        ).get()
        usersCommentLikeJpaRepository.delete(commentLikeEntity)
    }
}
