package com.example.springbootkotlinwebflux.endpoint.restapi.v1.account

import com.example.springbootkotlinwebflux.domain.authtoken.AuthToken
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable

@Serializable
data class AccountSignUpSignInResponseDto(

    val accessToken: String,
    val refreshToken: String,
    @get:JsonProperty("isNewAccount")
    val isNewAccount: Boolean
) {

    constructor(signUpSignIn: Pair<AuthToken, Boolean>): this(
        accessToken = signUpSignIn.first.accessToken,
        refreshToken = signUpSignIn.first.refreshToken,
        isNewAccount = signUpSignIn.second
    )
}
