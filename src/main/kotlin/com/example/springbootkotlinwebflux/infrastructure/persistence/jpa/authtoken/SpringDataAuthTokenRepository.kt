package com.example.springbootkotlinwebflux.infrastructure.persistence.jpa.authtoken

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface SpringDataAuthTokenRepository : R2dbcRepository<AuthTokenEntity, Long> {

    fun findTopByAccessTokenAndDeletedAtIsNullOrderByIdDesc(accessToken: String): Mono<AuthTokenEntity>
    fun findTopByAccountIdAndAccessTokenAndDeletedAtIsNullOrderByIdDesc(accountId: Long, accessToken: String): Mono<AuthTokenEntity>

    @Query("""
        UPDATE auth_token
        SET deleted_at = :deletedAt
        WHERE account_id = :accountId
            AND deleted_at IS NULL
    """)
    fun deleteAllByAccountIdAndDeletedAtIsNull(
        accountId: Long,
        deletedAt: LocalDateTime,
    ): Mono<Void>
}
