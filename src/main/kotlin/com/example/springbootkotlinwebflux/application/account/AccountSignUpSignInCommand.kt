package com.example.springbootkotlinwebflux.application.account

import com.example.springbootkotlinwebflux.domain.account.ThirdPartyAuthType
import com.example.springbootkotlinwebflux.domain.appuseenvironment.AppOS

class AccountSignUpSignInCommand {

    data class SignUpSignIn(
        val thirdPartyAuthType: ThirdPartyAuthType,
        val thirdPartyAuthUid: String,
        val email: String,
        val languageCode: String,
        val countryCode: String,
        val timeZoneCode: String,
        val deviceModelName: String,
        val appOs: AppOS,
        val appVersion: String,
        val appPushToken: String?
    )
}
