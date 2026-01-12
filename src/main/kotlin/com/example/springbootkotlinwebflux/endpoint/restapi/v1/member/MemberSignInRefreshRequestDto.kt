package com.example.springbootkotlinwebflux.endpoint.restapi.v1.member

import jakarta.validation.constraints.NotBlank
import kotlinx.serialization.Serializable

@Serializable
data class MemberSignInRefreshRequestDto(

    @field:NotBlank(message = "NotBlank")
    val accessToken: String? = null,
    @field:NotBlank(message = "NotBlank")
    val refreshToken: String? = null,
)
