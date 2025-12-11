package com.example.springbootkotlinwebflux.application.account

import com.example.springbootkotlinwebflux.configuration.logger.logger
import com.example.springbootkotlinwebflux.domain.appuseenvironment.AppUseEnvironmentRepository
import com.example.springbootkotlinwebflux.domain.authtoken.AuthTokenRepository
import com.example.springbootkotlinwebflux.exception.AuthTokenNotFoundException
import com.example.springbootkotlinwebflux.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AccountSignOutService(
    private val authTokenRepository: AuthTokenRepository,
    private val appUseEnvironmentRepository: AppUseEnvironmentRepository,
): AccountSignOutUseCase {

    @Transactional(rollbackFor = [Throwable::class])
    override suspend fun signOut(command: AccountSignOutCommand.SignOut) {
        try {
            val myAccountId = command.accountId
            val authToken = authTokenRepository.findTopByAccountIdAndAccessTokenAndDeletedAtIsNullOrderByIdDesc(
                accountId = myAccountId,
                accessToken = command.accessToken
            ) ?: throw AuthTokenNotFoundException(ErrorCode.ACCESS_TOKEN_NOT_FOUND)
            authTokenRepository.save(authToken, true)

            /*
            앱 사용 환경 정보 삭제
             */
            val currentLocalDateTime = LocalDateTime.now()
            appUseEnvironmentRepository.deleteAllByAccountIdAndDeviceModelNameAndDeletedAtIsNull(
                accountId = myAccountId,
                deviceModelName = command.deviceModelName,
                deletedAt = currentLocalDateTime
            )
        } catch (e: Throwable) {
            logger().error("command=$command", e)
            throw e
        }
    }
}
