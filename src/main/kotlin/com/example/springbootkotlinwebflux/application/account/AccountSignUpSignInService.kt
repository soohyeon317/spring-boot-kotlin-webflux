package com.example.springbootkotlinwebflux.application.account

import com.example.springbootkotlinwebflux.configuration.authentication.AuthenticationTokenManager
import com.example.springbootkotlinwebflux.configuration.authentication.AuthenticationTokenType
import com.example.springbootkotlinwebflux.configuration.logger.logger
import com.example.springbootkotlinwebflux.domain.account.Account
import com.example.springbootkotlinwebflux.domain.account.AccountRepository
import com.example.springbootkotlinwebflux.domain.account.ThirdPartyAuthType
import com.example.springbootkotlinwebflux.domain.authtoken.AuthToken
import com.example.springbootkotlinwebflux.domain.authtoken.AuthTokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountSignUpSignInService(
    private val accountRepository: AccountRepository,
    private val authTokenRepository: AuthTokenRepository,
    private val authenticationTokenManager: AuthenticationTokenManager,
) : AccountSignUpSignInUseCase {

    @Transactional(rollbackFor = [Throwable::class])
    // 회원가입 및 로그인 통합 처리
    // 멀티 로그인 비허용
    override suspend fun signUpSignIn(command: AccountSignUpSignInCommand.SignUpSignIn): Pair<AuthToken, Boolean> {
        try {

            var isNewAccount = false
            var myAccount: Account = accountRepository.findTopByThirdPartyAuthTypeAndThirdPartyAuthUidAndDeletedAtIsNullOrderByIdDesc(
                thirdPartyAuthType = command.thirdPartyAuthType,
                thirdPartyAuthUid = command.thirdPartyAuthUid
            ) ?: run {
                // 기존 계정이 없으면, 회원 가입 수행.
                isNewAccount = true
                signUp(
                    thirdPartyAuthType = command.thirdPartyAuthType,
                    thirdPartyAuthUid = command.thirdPartyAuthUid,
                    email = command.email,
                    languageCode = command.languageCode,
                    countryCode = command.countryCode,
                    timeZoneCode = command.timeZoneCode
                )
            }
            val myAccountId = myAccount.id!!

            /*
             기존 회원인 경우, 기존과 최신 정보를 비교하고 업데이트를 수행한다.
             */
            if (!isNewAccount) {
                val willUpdateEmail = (myAccount.email != command.email)
                val willUpdateLanguageCode = (myAccount.languageCode != command.languageCode)
                val willUpdateCountryCode = (myAccount.countryCode != command.countryCode)
                val willUpdateTimeZoneCode = (myAccount.timeZoneCode != command.timeZoneCode)
                if (willUpdateEmail || willUpdateLanguageCode || willUpdateCountryCode || willUpdateTimeZoneCode) {
                    myAccount = myAccount.updateEmail(
                        email = command.email
                    ).updateLocaleInfo(
                        languageCode = command.languageCode,
                        countryCode = command.countryCode,
                        timeZoneCode = command.timeZoneCode,
                    )
                    accountRepository.save(account = myAccount)
                }
            }

            /*
            인증 토큰 생성 및 응답
             */
            // 최근 authToken 조회
            val authToken = authTokenRepository.findTopByAccountIdAndDeletedAtIsNullOrderByIdDesc(accountId = myAccountId)
            // 이미 존재하면, 해당 authToken 삭제
            if (authToken != null) {
                authTokenRepository.save(authToken = authToken, willDelete = true)
            }
            // 새로운 authToken 저장
            val accessToken = authenticationTokenManager.createToken(accountId = myAccountId, tokenType = AuthenticationTokenType.ACCESS)
            val refreshToken = authenticationTokenManager.createToken(accountId = myAccountId, tokenType = AuthenticationTokenType.REFRESH)
            val newAuthToken = authTokenRepository.save(
                AuthToken(accountId = myAccountId, accessToken =  accessToken, refreshToken = refreshToken)
            )

            return Pair(newAuthToken, isNewAccount)
        } catch (e: Throwable) {
            logger().error("command=$command", e)
            throw e
        }
    }

    private suspend fun signUp(
        thirdPartyAuthType: ThirdPartyAuthType,
        thirdPartyAuthUid: String,
        email: String,
        languageCode: String,
        countryCode: String,
        timeZoneCode: String
    ): Account {
        val account = accountRepository.save(
            Account(
                thirdPartyAuthType = thirdPartyAuthType,
                thirdPartyAuthUid = thirdPartyAuthUid,
                email = email,
                languageCode = languageCode,
                countryCode = countryCode,
                timeZoneCode = timeZoneCode
            )
        )
        return account
    }
}
