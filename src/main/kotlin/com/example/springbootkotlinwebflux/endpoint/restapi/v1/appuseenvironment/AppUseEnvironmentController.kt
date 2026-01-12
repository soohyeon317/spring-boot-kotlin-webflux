package com.example.springbootkotlinwebflux.endpoint.restapi.v1.appuseenvironment

import com.example.springbootkotlinvirtualthread.configuration.annotation.FunctionExecutionBeforeLog
import com.example.springbootkotlinwebflux.application.appuseenvironment.AppPushTokenUpdateCommand
import com.example.springbootkotlinwebflux.application.appuseenvironment.AppPushTokenUpdateUseCase
import com.example.springbootkotlinwebflux.configuration.authentication.AuthenticationTokenManager
import com.example.springbootkotlinwebflux.domain.common.HeaderKey
import com.example.springbootkotlinwebflux.exception.ErrorCode
import com.example.springbootkotlinwebflux.exception.HeaderInvalidException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
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
        exchange: ServerWebExchange
    ) {
        val memberId = authenticationTokenManager.getMemberId()

        val deviceModelNameHeader = exchange.request.headers.getFirst(HeaderKey.DEVICE_MODEL_NAME)
        val appPushTokenHeader = exchange.request.headers.getFirst(HeaderKey.APP_PUSH_TOKEN)

        when {
            deviceModelNameHeader.isNullOrEmpty() -> {
                throw HeaderInvalidException(code = ErrorCode.DEVICE_MODEL_NAME_HEADER_REQUIRED)
            }

            appPushTokenHeader.isNullOrEmpty() -> {
                throw HeaderInvalidException(code = ErrorCode.APP_PUSH_TOKEN_HEADER_REQUIRED)
            }

            else -> {
                appPushTokenUpdateUseCase.updateAppPushToken(
                    command = AppPushTokenUpdateCommand.UpdateAppPushToken(
                        memberId = memberId,
                        deviceModelName = deviceModelNameHeader,
                        appPushToken = appPushTokenHeader
                    )
                )
            }
        }
    }
}
