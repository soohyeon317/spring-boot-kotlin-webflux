package com.example.springbootkotlinwebflux.configuration.authentication

interface AuthenticationTokenManager {

    fun validateToken(token: String, tokenType: AuthenticationTokenType): Boolean
    fun toAuthenticationToken(accessToken: String): AuthenticationToken
    fun createToken(memberId: Long, tokenType: AuthenticationTokenType): String
    fun getMemberIdFromToken(token: String): Long
    suspend fun isSaved(accessToken: String): Boolean
    suspend fun getMemberId(): Long
    suspend fun getDetails(): AuthenticationTokenDetails
}
