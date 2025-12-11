package com.example.springbootkotlinwebflux.endpoint.restapi.v1.account

import com.example.springbootkotlinvirtualthread.configuration.annotation.FunctionExecutionBeforeLog
import com.example.springbootkotlinwebflux.application.account.*
import com.example.springbootkotlinwebflux.configuration.authentication.AuthenticationToken
import com.example.springbootkotlinwebflux.configuration.authentication.AuthenticationTokenManager
import com.example.springbootkotlinwebflux.domain.account.AccountForResponse
import com.example.springbootkotlinwebflux.domain.account.LocaleInfoDefault
import com.example.springbootkotlinwebflux.domain.appuseenvironment.AppOS
import com.example.springbootkotlinwebflux.domain.common.HeaderKey
import com.example.springbootkotlinwebflux.exception.ErrorCode
import com.example.springbootkotlinwebflux.exception.HeaderInvalidException
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange

@RequestMapping("/api/v1/accounts")
@RestController
class AccountController(
    private val accountSignUpSignInUseCase: AccountSignUpSignInUseCase,
    private val accountSignInRefreshUseCase: AccountSignInRefreshUseCase,
    private val accountDetailGetUseCase: AccountDetailGetUseCase,
    private val accountSignOutUseCase: AccountSignOutUseCase,
    private val accountWithdrawUseCase: AccountWithdrawUseCase,
    private val authenticationTokenManager: AuthenticationTokenManager,
) {

    @FunctionExecutionBeforeLog
    @PostMapping("/sign-up/sign-in")
    @ResponseStatus(HttpStatus.OK)
    suspend fun signUpSignIn(
        exchange: ServerWebExchange,
        @RequestBody @Valid request: AccountSignUpSignInRequestDto
    ): AccountSignUpSignInResponseDto {
        val languageCodeHeader = exchange.request.headers.getFirst(HeaderKey.LANGUAGE_CODE) ?: LocaleInfoDefault.LANGUAGE_CODE.default
        val countryCodeHeader = exchange.request.headers.getFirst(HeaderKey.COUNTRY_CODE) ?: LocaleInfoDefault.COUNTRY_CODE.default
        val timeZoneCodeHeader = exchange.request.headers.getFirst(HeaderKey.TIME_ZONE_CODE) ?: LocaleInfoDefault.TIME_ZONE_CODE.default

        val deviceModelNameHeader = exchange.request.headers.getFirst(HeaderKey.DEVICE_MODEL_NAME)
        val appOsHeader = exchange.request.headers.getFirst(HeaderKey.APP_OS)
        val appVersionHeader = exchange.request.headers.getFirst(HeaderKey.APP_VERSION)
        val appPushToken = request.appPushToken

        when {
            deviceModelNameHeader.isNullOrEmpty() -> {
                throw HeaderInvalidException(code = ErrorCode.DEVICE_MODEL_NAME_HEADER_REQUIRED)
            }
            appOsHeader.isNullOrEmpty() -> {
                throw HeaderInvalidException(code = ErrorCode.APP_OS_HEADER_REQUIRED)
            }
            appVersionHeader.isNullOrEmpty() -> {
                throw HeaderInvalidException(code = ErrorCode.APP_VERSION_HEADER_REQUIRED)
            }
            else -> {
                val appOs = AppOS.getBy(appOs = appOsHeader)

                return AccountSignUpSignInResponseDto(
                    accountSignUpSignInUseCase.signUpSignIn(
                        AccountSignUpSignInCommand.SignUpSignIn(
                            thirdPartyAuthType = request.thirdPartyAuthType!!,
                            thirdPartyAuthUid = request.thirdPartyAuthUid!!,
                            email = request.email!!,
                            languageCode = languageCodeHeader,
                            countryCode = countryCodeHeader,
                            timeZoneCode = timeZoneCodeHeader,
                            deviceModelName = deviceModelNameHeader,
                            appOs = appOs,
                            appVersion = appVersionHeader,
                            appPushToken = appPushToken,
                        )
                    )
                )
            }
        }
    }

    @FunctionExecutionBeforeLog
    @PostMapping("/sign-in/refresh")
    @ResponseStatus(HttpStatus.OK)
    suspend fun refreshSignIn(
        exchange: ServerWebExchange,
        @RequestBody @Valid request: AccountSignInRefreshRequestDto
    ): AccountSignInRefreshResponseDto {
        val deviceModelNameHeader = exchange.request.headers.getFirst(HeaderKey.DEVICE_MODEL_NAME)
        val appOsHeader = exchange.request.headers.getFirst(HeaderKey.APP_OS)
        val appVersionHeader = exchange.request.headers.getFirst(HeaderKey.APP_VERSION)
        val appPushToken = request.appPushToken

        when {
            deviceModelNameHeader.isNullOrEmpty() -> {
                throw HeaderInvalidException(code = ErrorCode.DEVICE_MODEL_NAME_HEADER_REQUIRED)
            }

            appOsHeader.isNullOrEmpty() -> {
                throw HeaderInvalidException(code = ErrorCode.APP_OS_HEADER_REQUIRED)
            }

            appVersionHeader.isNullOrEmpty() -> {
                throw HeaderInvalidException(code = ErrorCode.APP_VERSION_HEADER_REQUIRED)
            }

            else -> {
                val appOs = AppOS.getBy(appOs = appOsHeader)

                return AccountSignInRefreshResponseDto(
                    authToken = accountSignInRefreshUseCase.refreshSignIn(
                        command = AccountSignInRefreshCommand.RefreshSignIn(
                            accessToken = request.accessToken!!,
                            refreshToken = request.refreshToken!!,
                            deviceModelName = deviceModelNameHeader,
                            appOs = appOs,
                            appVersion = appVersionHeader,
                            appPushToken = appPushToken
                        )
                    )
                )
            }
        }
    }

    @FunctionExecutionBeforeLog
    @GetMapping("/detail")
    @ResponseStatus(HttpStatus.OK)
    suspend fun getAccountDetail(
        exchange: ServerWebExchange
    ): AccountForResponse {
        val languageCode = exchange.request.headers.getFirst(HeaderKey.LANGUAGE_CODE) ?: LocaleInfoDefault.LANGUAGE_CODE.default
        val countryCode = exchange.request.headers.getFirst(HeaderKey.COUNTRY_CODE) ?: LocaleInfoDefault.COUNTRY_CODE.default
        val timeZoneCode = exchange.request.headers.getFirst(HeaderKey.TIME_ZONE_CODE) ?: LocaleInfoDefault.TIME_ZONE_CODE.default
        return accountDetailGetUseCase.getAccountDetail(
            AccountDetailGetCommand.GetAccountDetail(
                accountId = authenticationTokenManager.getAccountId(),
                languageCode = languageCode,
                countryCode = countryCode,
                timeZoneCode = timeZoneCode,
            )
        )
    }

    @FunctionExecutionBeforeLog
    @PostMapping("/sign-out")
    @ResponseStatus(HttpStatus.OK)
    suspend fun signOut(
        exchange: ServerWebExchange
    ) {
        val accessToken = AuthenticationToken.getJwtFromExchange(exchange = exchange)!!
        val accountId = authenticationTokenManager.getAccountIdFromToken(token = accessToken)

        val deviceModelNameHeader = exchange.request.headers.getFirst(HeaderKey.DEVICE_MODEL_NAME)

        when {
            deviceModelNameHeader.isNullOrEmpty() -> {
                throw HeaderInvalidException(code = ErrorCode.DEVICE_MODEL_NAME_HEADER_REQUIRED)
            }
            else -> {
                accountSignOutUseCase.signOut(
                    command = AccountSignOutCommand.SignOut(
                        accountId = accountId,
                        accessToken = accessToken,
                        deviceModelName = deviceModelNameHeader,
                    )
                )
            }
        }
    }

    @FunctionExecutionBeforeLog
    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    suspend fun withdraw() {
        val accountId = authenticationTokenManager.getAccountId()
        accountWithdrawUseCase.withdraw(
            command = AccountWithdrawCommand.Withdraw(accountId = accountId)
        )
    }
}
