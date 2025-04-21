package com.backend.sodam.domain.secrets.service

import com.backend.sodam.domain.secrets.service.response.SecretCreateResponse
import com.backend.sodam.domain.secrets.service.response.SecretDetailResponse
import com.backend.sodam.domain.secrets.service.response.SecretSummaryResponse
import com.backend.sodam.domain.secrets.exception.SecretException
import com.backend.sodam.domain.secrets.service.command.SecretCreateCommand
import com.backend.sodam.domain.secrets.service.command.SecretSearchCommand
import com.backend.sodam.domain.secrets.service.port.CreateSecretViewPort
import com.backend.sodam.domain.secrets.service.port.FetchSecretPort
import com.backend.sodam.domain.secrets.service.port.FetchSecretViewPort
import com.backend.sodam.domain.secrets.service.port.UpdateSecretPort
import com.backend.sodam.domain.secrets.service.usecase.CreateSecretUseCase
import com.backend.sodam.domain.secrets.service.usecase.DeleteSecretUseCase
import com.backend.sodam.domain.secrets.service.usecase.FetchSecretUseCase
import com.backend.sodam.domain.secrets.service.usecase.UpdateSecretUseCase
import com.backend.sodam.domain.users.exception.UserException
import com.backend.sodam.domain.users.model.UserType
import com.backend.sodam.domain.users.service.port.FetchUserPort
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class SecretService(
    private val fetchUserPorts: List<FetchUserPort>,
    private val fetchSecretPort: FetchSecretPort,
    private val updateSecretPort: UpdateSecretPort,
    private val fetchSecretViewPorts: List<FetchSecretViewPort>,
    private val createSecretViewPorts: List<CreateSecretViewPort>,
    private val viewValidators: List<SecretViewValidator>
): CreateSecretUseCase, FetchSecretUseCase, UpdateSecretUseCase, DeleteSecretUseCase {

    override fun fetchFromClient(pageable: Pageable, secretSearchCommand: SecretSearchCommand): Page<SecretSummaryResponse> {
        return fetchSecretPort.findByPageBy(pageable = pageable, secretSearchCommand = secretSearchCommand)
                              .map {it.toSummaryResponse()}
    }

    @Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = [Exception::class]
    )
    override fun getSecretDetail(userId: String, secretId: Long, role: String): SecretDetailResponse {
        checkExistsSecret(secretId = secretId)

        val userType = extractUserType(userId = userId)
        val createSecretViewPort = getCreateSecretViewPort(userType = userType)
        val fetchSecretViewPort = getFetchSecretViewPort()
        val todayTotalViewCount = fetchSecretViewPort.countViewToday(userId = userId) // 보유 구독권 서비스에서 현재 회원의 당일 조회수 확인
        val isViewable = viewValidators.stream()
                                                 .filter { it.isTarget(role) } // 현재 발급된 구독권
                                                 .findFirst()
                                                 .orElseThrow()
                                                 .isValidView(todayTotalViewCount) // 조회 가능 여부 확인

        if ( ! isViewable )
            throw SecretException.InvalidSecretViewException()


        updateSecretPort.increaseViewCnt(secretId) // 조회수 증가
        createSecretViewPort.create(userId = userId, secretId = secretId) // 시청 이력 생성
        return fetchSecretPort.findDetailBySecretId(secretId = secretId)
                              .toResponse()
    }

    private fun checkExistsSecret(secretId: Long) {
        if ( ! isExistsSecret(secretId = secretId) )
            throw SecretException.SecretNotFoundException()
    }

    private fun isExistsSecret(secretId: Long): Boolean =
        fetchSecretPort.isExistsSecret(secretId)

    private fun extractUserType(userId: String): UserType {
        val fetchPort = getFetchPortByUserId(userId = userId)
        val sodamUser = fetchPort.findByUserId(userId = userId).get()
        return sodamUser.userType
    }

    private fun getFetchPortByUserId(userId: String): FetchUserPort =
        fetchUserPorts.stream()
            .filter { it.isExistsByUserId(userId) }
            .findFirst()
            .orElseThrow { UserException.UserNotFoundException() }

    private fun getFetchSecretViewPort(): FetchSecretViewPort =
        fetchSecretViewPorts.stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

    private fun getCreateSecretViewPort(userType: UserType): CreateSecretViewPort =
        createSecretViewPorts.stream()
            .filter { it.isTarget(userType = userType) }
            .findFirst()
            .orElseThrow { IllegalArgumentException() }
}
