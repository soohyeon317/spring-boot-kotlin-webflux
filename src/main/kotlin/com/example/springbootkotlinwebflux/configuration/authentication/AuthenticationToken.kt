package com.example.springbootkotlinwebflux.configuration.authentication

import io.jsonwebtoken.Claims
import org.springframework.http.HttpHeaders
import org.springframework.util.StringUtils
import org.springframework.web.server.ServerWebExchange
import java.util.*

data class AuthenticationToken(
    val accountId: Long,
    val expiration: Long,
    val expirationDate: Date,
    val tokenType: AuthenticationTokenType
) {

    constructor(claims: Claims) : this(
        accountId = claims[ACCOUNT_ID_CLAIM_KEY].toString().toLong(),
        expiration = claims.expiration.toInstant().toEpochMilli(),
        expirationDate = claims.expiration,
        tokenType = AuthenticationTokenType.valueOf(claims[TOKEN_TYPE_CLAIM_KEY] as String)
    )

    companion object {
        const val ACCOUNT_ID_CLAIM_KEY = "accountId"
        const val TOKEN_TYPE_CLAIM_KEY = "tokenType"
        const val BEARER_TOKEN_PREFIX = "Bearer "

        fun getJwtFromExchange(exchange: ServerWebExchange): String? {
            val bearerToken = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)!!
            return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TOKEN_PREFIX)) {
                bearerToken.substring(BEARER_TOKEN_PREFIX.length)
            } else {
                null
            }
        }
    }
}
