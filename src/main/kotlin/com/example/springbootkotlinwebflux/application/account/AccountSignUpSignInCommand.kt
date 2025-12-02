package com.example.springbootkotlinwebflux.application.account

import com.example.springbootkotlinwebflux.domain.account.ThirdPartyAuthType

class AccountSignUpSignInCommand {

    data class SignUpSignIn(
        val thirdPartyAuthType: ThirdPartyAuthType,
        val thirdPartyAuthUid: String,
        val email: String,
        val languageCode: String,
        val countryCode: String,
        val timeZoneCode: String
    )
}
