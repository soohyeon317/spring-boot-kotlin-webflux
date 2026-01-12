package com.example.springbootkotlinwebflux.endpoint.restapi.v1.member

import com.example.springbootkotlinwebflux.domain.authtoken.AuthToken
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.Serializable

@Serializable
data class MemberSignUpSignInResponseDto(

    val accessToken: String,
    val refreshToken: String,
    @get:JsonProperty("isNewMember")
    val isNewMember: Boolean
) {

    constructor(signUpSignIn: Pair<AuthToken, Boolean>): this(
        accessToken = signUpSignIn.first.accessToken,
        refreshToken = signUpSignIn.first.refreshToken,
        isNewMember = signUpSignIn.second
    )
}
