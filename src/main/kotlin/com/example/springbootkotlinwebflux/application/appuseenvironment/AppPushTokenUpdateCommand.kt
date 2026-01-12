package com.example.springbootkotlinwebflux.application.appuseenvironment

class AppPushTokenUpdateCommand {

    data class UpdateAppPushToken(
        val memberId: Long,
        val deviceModelName: String,
        val appPushToken: String,
    )
}
