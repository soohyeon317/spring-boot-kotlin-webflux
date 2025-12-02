package com.example.springbootkotlinwebflux.endpoint.restapi.v1.account

import com.example.springbootkotlinvirtualthread.configuration.annotation.FunctionExecutionBeforeLog
import com.example.springbootkotlinwebflux.application.account.*
import com.example.springbootkotlinwebflux.configuration.authentication.AuthenticationTokenManager
import com.example.springbootkotlinwebflux.domain.account.AccountForResponse
import com.example.springbootkotlinwebflux.domain.account.LocaleInfoDefault
import com.example.springbootkotlinwebflux.domain.common.HeaderKey
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
    private val authenticationTokenManager: AuthenticationTokenManager,
) {

    @FunctionExecutionBeforeLog
    @PostMapping("/sign-up/sign-in")
    @ResponseStatus(HttpStatus.OK)
    suspend fun signUpSignIn(
        exchange: ServerWebExchange,
        @RequestBody @Valid request: AccountSignUpSignInRequestDto
    ): AccountSignUpSignInResponseDto {
        val languageCode = exchange.request.headers.getFirst(HeaderKey.LANGUAGE_CODE) ?: LocaleInfoDefault.LANGUAGE_CODE.default
        val countryCode = exchange.request.headers.getFirst(HeaderKey.COUNTRY_CODE) ?: LocaleInfoDefault.COUNTRY_CODE.default
        val timeZoneCode = exchange.request.headers.getFirst(HeaderKey.TIME_ZONE_CODE) ?: LocaleInfoDefault.TIME_ZONE_CODE.default
        return AccountSignUpSignInResponseDto(
            accountSignUpSignInUseCase.signUpSignIn(
                AccountSignUpSignInCommand.SignUpSignIn(
                    thirdPartyAuthType = request.thirdPartyAuthType!!,
                    thirdPartyAuthUid = request.thirdPartyAuthUid!!,
                    email = request.email!!,
                    languageCode = languageCode,
                    countryCode = countryCode,
                    timeZoneCode = timeZoneCode,
                )
            )
        )
    }

    @PostMapping("/sign-in/refresh")
    @ResponseStatus(HttpStatus.OK)
    suspend fun refreshSignIn(
        @RequestBody @Valid request: AccountSignInRefreshRequestDto
    ): AccountSignInRefreshResponseDto {
        return AccountSignInRefreshResponseDto(
            accountSignInRefreshUseCase.refreshSignIn(
                AccountSignInRefreshCommand.RefreshSignIn(
                    accessToken = request.accessToken!!,
                    refreshToken = request.refreshToken!!
                )))
    }

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
}
