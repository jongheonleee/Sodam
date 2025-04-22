package com.backend.sodam.domain.comments.repository

import com.backend.sodam.domain.comments.entity.UsersDislikeCommentEntity
import com.backend.sodam.domain.comments.service.port.CreateUserCommentDislikePort
import com.backend.sodam.domain.comments.service.port.DeleteUserCommentDislikePort
import com.backend.sodam.domain.comments.service.port.FetchUserCommentDislikePort
import com.backend.sodam.domain.comments.service.port.UpdateUserCommentDislikePort
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.repository.NormalUserJpaRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@RequiredArgsConstructor
class NormalUsersCommentDislikeRepository(
    private val commentJpaRepository: CommentJpaRepository,
    private val normalUserJpaRepository: NormalUserJpaRepository,
    private val usersCommentDislikeJpaRepository: UsersCommentDislikeJpaRepository
) : CreateUserCommentDislikePort, FetchUserCommentDislikePort, UpdateUserCommentDislikePort, DeleteUserCommentDislikePort {

    override fun isTarget(userType: UserType): Boolean =
        UserType.NORMAL == userType

    @Transactional
    override fun createDislike(userId: String, commentId: Long) {
        val userEntity = normalUserJpaRepository.findByUserId(userId).get()
        val commentEntity = commentJpaRepository.findByCommentId(commentId).get()
        val userCommentDislikeEntity = UsersDislikeCommentEntity(
            user = userEntity,
            comment = commentEntity
        )
        usersCommentDislikeJpaRepository.save(userCommentDislikeEntity)
    }

    @Transactional(readOnly = true)
    override fun existsCommentDislike(commentId: Long, userId: String): Boolean {
        val userEntity = normalUserJpaRepository.findByUserId(userId).get()
        val commentEntity = commentJpaRepository.findByCommentId(commentId).get()
        return usersCommentDislikeJpaRepository.existsByCommentAndUser(
            user = userEntity,
            comment = commentEntity
        )
    }

    @Transactional
    override fun deleteDislike(commentId: Long, userId: String) {
        val userEntity = normalUserJpaRepository.findByUserId(userId = userId).get()
        val commentEntity = commentJpaRepository.findByCommentId(commentId = commentId).get()
        val commentDislikeEntity = usersCommentDislikeJpaRepository.findByCommentAndUser(
            user = userEntity,
            comment = commentEntity
        ).get()
        usersCommentDislikeJpaRepository.delete(commentDislikeEntity)
    }
}
