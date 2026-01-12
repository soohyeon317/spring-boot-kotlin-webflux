package com.example.springbootkotlinwebflux.infrastructure.persistence.jpa.authtoken

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface SpringDataAuthTokenRepository : R2dbcRepository<AuthTokenEntity, Long> {

    fun findTopByAccessTokenAndDeletedAtIsNullOrderByIdDesc(accessToken: String): Mono<AuthTokenEntity>
    fun findTopByMemberIdAndAccessTokenAndDeletedAtIsNullOrderByIdDesc(memberId: Long, accessToken: String): Mono<AuthTokenEntity>

    @Query("""
        UPDATE auth_token
        SET deleted_at = :deletedAt
        WHERE member_id = :memberId
            AND deleted_at IS NULL
    """)
    fun deleteAllByMemberIdAndDeletedAtIsNull(
        memberId: Long,
        deletedAt: LocalDateTime,
    ): Mono<Void>
}
