package com.example.springbootkotlinwebflux.application.appuseenvironment

fun interface AppPushTokenUpdateUseCase {

    suspend fun updateAppPushToken(command: AppPushTokenUpdateCommand.UpdateAppPushToken)
}
