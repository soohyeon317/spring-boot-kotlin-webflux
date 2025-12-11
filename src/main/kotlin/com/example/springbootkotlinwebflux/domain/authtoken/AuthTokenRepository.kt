package com.example.springbootkotlinwebflux.domain.authtoken

import java.time.LocalDateTime

interface AuthTokenRepository {

    suspend fun save(authToken: AuthToken, willDelete: Boolean? = null): AuthToken
    suspend fun findTopByAccountIdAndDeletedAtIsNullOrderByIdDesc(accountId: Long): AuthToken?
    suspend fun findTopByAccessTokenAndDeletedAtIsNullOrderByIdDesc(accessToken: String): AuthToken?
    suspend fun findTopByAccountIdAndAccessTokenAndDeletedAtIsNullOrderByIdDesc(accountId: Long, accessToken: String): AuthToken?
    suspend fun findTopByAccountIdOrderByIdDesc(accountId: Long): AuthToken?

    suspend fun deleteAllByAccountIdAndDeletedAtIsNull(
        accountId: Long,
        deletedAt: LocalDateTime,
    )
}
