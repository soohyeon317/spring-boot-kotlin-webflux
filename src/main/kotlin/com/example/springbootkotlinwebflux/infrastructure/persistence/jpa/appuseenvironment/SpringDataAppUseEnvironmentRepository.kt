package com.example.springbootkotlinwebflux.infrastructure.persistence.jpa.appuseenvironment

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface SpringDataAppUseEnvironmentRepository : R2dbcRepository<AppUseEnvironmentEntity, Long> {

    fun findTopByAccountIdAndDeviceModelNameAndDeletedAtIsNullOrderByIdDesc(
        accountId: Long,
        deviceModelName: String,
    ): Mono<AppUseEnvironmentEntity>

    @Query("""
        UPDATE app_use_environment
        SET deleted_at = :deletedAt
        WHERE account_id = :accountId
            AND device_model_name = :deviceModelName
            AND deleted_at IS NULL
    """)
    fun deleteAllByAccountIdAndDeviceModelNameAndDeletedAtIsNull(
        accountId: Long,
        deviceModelName: String,
        deletedAt: LocalDateTime,
    ): Mono<Void>

    @Query("""
        UPDATE app_use_environment
        SET deleted_at = :deletedAt
        WHERE account_id = :accountId
            AND deleted_at IS NULL
    """)
    fun deleteAllByAccountIdAndDeletedAtIsNull(
        accountId: Long,
        deletedAt: LocalDateTime,
    ): Mono<Void>
}
