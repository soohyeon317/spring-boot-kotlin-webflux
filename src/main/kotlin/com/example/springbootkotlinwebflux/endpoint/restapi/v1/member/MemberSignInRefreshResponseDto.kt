package com.example.springbootkotlinwebflux.endpoint.restapi.v1.member

import com.example.springbootkotlinwebflux.domain.authtoken.AuthToken
import kotlinx.serialization.Serializable

@Serializable
data class MemberSignInRefreshResponseDto(
    val accessToken: String,
    val refreshToken: String,
) {

    constructor(authToken: AuthToken): this(
        accessToken = authToken.accessToken,
        refreshToken = authToken.refreshToken,
    )
}
