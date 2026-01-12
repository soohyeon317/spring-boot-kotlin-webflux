package com.example.springbootkotlinwebflux.endpoint.restapi.v1.member

import com.example.springbootkotlinvirtualthread.configuration.annotation.FunctionExecutionBeforeLog
import com.example.springbootkotlinwebflux.application.member.*
import com.example.springbootkotlinwebflux.configuration.authentication.AuthenticationToken
import com.example.springbootkotlinwebflux.configuration.authentication.AuthenticationTokenManager
import com.example.springbootkotlinwebflux.domain.member.MemberForResponse
import com.example.springbootkotlinwebflux.domain.member.LocaleInfoDefault
import com.example.springbootkotlinwebflux.domain.appuseenvironment.AppOS
import com.example.springbootkotlinwebflux.domain.common.HeaderKey
import com.example.springbootkotlinwebflux.exception.ErrorCode
import com.example.springbootkotlinwebflux.exception.HeaderInvalidException
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange

@RequestMapping("/api/v1/members")
@RestController
class MemberController(
    private val memberSignUpSignInUseCase: MemberSignUpSignInUseCase,
    private val memberSignInRefreshUseCase: MemberSignInRefreshUseCase,
    private val memberDetailsGetUseCase: MemberDetailsGetUseCase,
    private val memberSignOutUseCase: MemberSignOutUseCase,
    private val memberWithdrawUseCase: MemberWithdrawUseCase,
    private val authenticationTokenManager: AuthenticationTokenManager,
) {

    @FunctionExecutionBeforeLog
    @PostMapping("/sign-up/sign-in")
    @ResponseStatus(HttpStatus.OK)
    suspend fun signUpSignIn(
        exchange: ServerWebExchange,
        @RequestBody @Valid request: MemberSignUpSignInRequestDto
    ): MemberSignUpSignInResponseDto {
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

                return MemberSignUpSignInResponseDto(
                    memberSignUpSignInUseCase.signUpSignIn(
                        MemberSignUpSignInCommand.SignUpSignIn(
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
        @RequestBody @Valid request: MemberSignInRefreshRequestDto
    ): MemberSignInRefreshResponseDto {
        val deviceModelNameHeader = exchange.request.headers.getFirst(HeaderKey.DEVICE_MODEL_NAME)
        val appOsHeader = exchange.request.headers.getFirst(HeaderKey.APP_OS)
        val appVersionHeader = exchange.request.headers.getFirst(HeaderKey.APP_VERSION)
        val appPushTokenHeader = exchange.request.headers.getFirst(HeaderKey.APP_PUSH_TOKEN)

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

                return MemberSignInRefreshResponseDto(
                    authToken = memberSignInRefreshUseCase.refreshSignIn(
                        command = MemberSignInRefreshCommand.RefreshSignIn(
                            accessToken = request.accessToken!!,
                            refreshToken = request.refreshToken!!,
                            deviceModelName = deviceModelNameHeader,
                            appOs = appOs,
                            appVersion = appVersionHeader,
                            appPushToken = appPushTokenHeader
                        )
                    )
                )
            }
        }
    }

    @FunctionExecutionBeforeLog
    @GetMapping("/details")
    @ResponseStatus(HttpStatus.OK)
    suspend fun getMemberDetails(
        exchange: ServerWebExchange
    ): MemberForResponse {
        val languageCode = exchange.request.headers.getFirst(HeaderKey.LANGUAGE_CODE) ?: LocaleInfoDefault.LANGUAGE_CODE.default
        val countryCode = exchange.request.headers.getFirst(HeaderKey.COUNTRY_CODE) ?: LocaleInfoDefault.COUNTRY_CODE.default
        val timeZoneCode = exchange.request.headers.getFirst(HeaderKey.TIME_ZONE_CODE) ?: LocaleInfoDefault.TIME_ZONE_CODE.default
        return memberDetailsGetUseCase.getMemberDetails(
            MemberDetailsGetCommand.GetMemberDetails(
                memberId = authenticationTokenManager.getMemberId(),
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
        val memberId = authenticationTokenManager.getMemberIdFromToken(token = accessToken)

        val deviceModelNameHeader = exchange.request.headers.getFirst(HeaderKey.DEVICE_MODEL_NAME)

        when {
            deviceModelNameHeader.isNullOrEmpty() -> {
                throw HeaderInvalidException(code = ErrorCode.DEVICE_MODEL_NAME_HEADER_REQUIRED)
            }
            else -> {
                memberSignOutUseCase.signOut(
                    command = MemberSignOutCommand.SignOut(
                        memberId = memberId,
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
        val memberId = authenticationTokenManager.getMemberId()
        memberWithdrawUseCase.withdraw(
            command = MemberWithdrawCommand.Withdraw(memberId = memberId)
        )
    }
}
