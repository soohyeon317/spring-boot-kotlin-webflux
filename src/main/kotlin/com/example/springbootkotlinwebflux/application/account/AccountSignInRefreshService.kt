package com.example.springbootkotlinwebflux.application.account

import com.example.springbootkotlinwebflux.configuration.authentication.AuthenticationTokenManager
import com.example.springbootkotlinwebflux.configuration.authentication.AuthenticationTokenType
import com.example.springbootkotlinwebflux.configuration.logger.logger
import com.example.springbootkotlinwebflux.domain.account.AccountRepository
import com.example.springbootkotlinwebflux.domain.appuseenvironment.AppUseEnvironmentManager
import com.example.springbootkotlinwebflux.domain.authtoken.AuthToken
import com.example.springbootkotlinwebflux.domain.authtoken.AuthTokenRepository
import com.example.springbootkotlinwebflux.exception.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountSignInRefreshService(
    private val accountRepository: AccountRepository,
    private val authTokenRepository: AuthTokenRepository,
    private val authenticationTokenManager: AuthenticationTokenManager,
    private val appUseEnvironmentManager: AppUseEnvironmentManager,
): AccountSignInRefreshUseCase {

    @Transactional(rollbackFor = [Throwable::class])
    override suspend fun refreshSignIn(command: AccountSignInRefreshCommand.RefreshSignIn): AuthToken {
        try {
            // 해당 AccessToken 정보와 일치하는 AuthToken 데이터 가져오기
            val authToken = authTokenRepository.findTopByAccessTokenAndDeletedAtIsNullOrderByIdDesc(command.accessToken)
                ?: throw AuthTokenNotFoundException(ErrorCode.ACCESS_TOKEN_NOT_FOUND)
            accountRepository.findTopByIdAndDeletedAtIsNull(authToken.accountId)
                ?: throw AccountNotFoundException(ErrorCode.MY_ACCOUNT_NOT_FOUND)
            val myAccountId = authToken.accountId

            // RefreshToken 유효성 검증
            authenticationTokenManager.validateToken(command.refreshToken, AuthenticationTokenType.REFRESH)
            // RefreshToken 일치 여부 검사
            if (command.refreshToken != authToken.refreshToken) {
                throw InputInvalidException(ErrorCode.REFRESH_TOKEN_NOT_MATCHED)
            }

            // 새로운 토큰 생성
            val newAccessToken =
                authenticationTokenManager.createToken(authToken.accountId, AuthenticationTokenType.ACCESS)
            val newRefreshToken =
                authenticationTokenManager.createToken(authToken.accountId, AuthenticationTokenType.REFRESH)

            /*
            앱 사용 환경 정보 저장
             */
            appUseEnvironmentManager.create(
                accountId = myAccountId,
                deviceModelName = command.deviceModelName,
                appOs = command.appOs,
                appVersion = command.appVersion,
                appPushToken = command.appPushToken,
            )

            return authTokenRepository.save(
                authToken.update(newAccessToken, newRefreshToken)
            )
        } catch (e: Throwable) {
            if (e is UnAuthorizedException) {
                logger().warn("command=$command", e)
            } else {
                logger().error("command=$command", e)
            }
            throw e
        }
    }
}
