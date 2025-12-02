package com.example.springbootkotlinwebflux.infrastructure.persistence.jpa.authtoken

import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface SpringDataAuthTokenRepository : R2dbcRepository<AuthTokenEntity, Long> {

    fun findTopByAccountIdAndDeletedAtIsNullOrderByIdDesc(accountId: Long): Mono<AuthTokenEntity>
    fun findTopByAccessTokenAndDeletedAtIsNullOrderByIdDesc(accessToken: String): Mono<AuthTokenEntity>
    fun findTopByAccountIdAndAccessTokenAndDeletedAtIsNullOrderByIdDesc(accountId: Long, accessToken: String): Mono<AuthTokenEntity>
    fun findAllByAccountIdAndDeletedAtIsNull(accountId: Long): Flux<AuthTokenEntity>
    fun findTopByAccountIdOrderByIdDesc(accountId: Long): Mono<AuthTokenEntity>
}
