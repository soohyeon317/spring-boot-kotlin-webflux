package com.example.springbootkotlinwebflux.endpoint.restapi.v1.appuseenvironment

import com.example.springbootkotlinvirtualthread.configuration.annotation.FunctionExecutionBeforeLog
import com.example.springbootkotlinvirtualthread.endpoint.restapi.v1.appuseenvironment.AppPushTokenUpdateRequestDto
import com.example.springbootkotlinwebflux.application.appuseenvironment.AppPushTokenUpdateCommand
import com.example.springbootkotlinwebflux.application.appuseenvironment.AppPushTokenUpdateUseCase
import com.example.springbootkotlinwebflux.configuration.authentication.AuthenticationTokenManager
import com.example.springbootkotlinwebflux.domain.common.HeaderKey
import com.example.springbootkotlinwebflux.exception.ErrorCode
import com.example.springbootkotlinwebflux.exception.HeaderInvalidException
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange

@RestController
@RequestMapping("/api/v1/app-use-environments")
class AppUseEnvironmentController(
    private val appPushTokenUpdateUseCase: AppPushTokenUpdateUseCase,
    private val authenticationTokenManager: AuthenticationTokenManager,
) {

    @FunctionExecutionBeforeLog
    @PutMapping("/app-push-token")
    @ResponseStatus(HttpStatus.OK)
    suspend fun updateAppPushToken(
        exchange: ServerWebExchange,
        @RequestBody @Valid request: AppPushTokenUpdateRequestDto
    ) {
        val accountId = authenticationTokenManager.getAccountId()

        val deviceModelNameHeader = exchange.request.headers.getFirst(HeaderKey.DEVICE_MODEL_NAME)
        val appPushToken = request.appPushToken!!

        when {
            deviceModelNameHeader.isNullOrEmpty() -> {
                throw HeaderInvalidException(code = ErrorCode.DEVICE_MODEL_NAME_HEADER_REQUIRED)
            }

            else -> {
                appPushTokenUpdateUseCase.updateAppPushToken(
                    command = AppPushTokenUpdateCommand.UpdateAppPushToken(
                        accountId = accountId,
                        deviceModelName = deviceModelNameHeader,
                        appPushToken = appPushToken
                    )
                )
            }
        }
    }
}
