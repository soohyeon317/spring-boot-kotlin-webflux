package com.example.springbootkotlinwebflux.configuration.authentication

interface AuthenticationTokenManager {

    fun validateToken(token: String, tokenType: AuthenticationTokenType): Boolean
    fun toAuthenticationToken(accessToken: String): AuthenticationToken
    fun createToken(accountId: Long, tokenType: AuthenticationTokenType): String
    fun getAccountIdFromToken(token: String): Long
    suspend fun isSaved(accessToken: String): Boolean
    suspend fun getAccountId(): Long
    suspend fun getDetails(): AuthenticationTokenDetails
}
