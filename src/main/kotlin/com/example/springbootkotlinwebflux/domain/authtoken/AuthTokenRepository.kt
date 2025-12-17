package com.example.springbootkotlinwebflux.domain.authtoken

import java.time.LocalDateTime

interface AuthTokenRepository {

    suspend fun save(authToken: AuthToken, willDelete: Boolean = false): AuthToken
    suspend fun findTopByAccessTokenAndDeletedAtIsNullOrderByIdDesc(accessToken: String): AuthToken?
    suspend fun findTopByAccountIdAndAccessTokenAndDeletedAtIsNullOrderByIdDesc(accountId: Long, accessToken: String): AuthToken?

    suspend fun deleteAllByAccountIdAndDeletedAtIsNull(
        accountId: Long,
        deletedAt: LocalDateTime,
    )
}
