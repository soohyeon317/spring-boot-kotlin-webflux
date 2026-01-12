package com.example.springbootkotlinwebflux.infrastructure.persistence.jpa.appuseenvironment

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

interface SpringDataAppUseEnvironmentRepository : R2dbcRepository<AppUseEnvironmentEntity, Long> {

    fun findTopByMemberIdAndDeviceModelNameAndDeletedAtIsNullOrderByIdDesc(
        memberId: Long,
        deviceModelName: String,
    ): Mono<AppUseEnvironmentEntity>

    @Query("""
        UPDATE app_use_environment
        SET deleted_at = :deletedAt
        WHERE member_id = :memberId
            AND device_model_name = :deviceModelName
            AND deleted_at IS NULL
    """)
    fun deleteAllByMemberIdAndDeviceModelNameAndDeletedAtIsNull(
        memberId: Long,
        deviceModelName: String,
        deletedAt: LocalDateTime,
    ): Mono<Void>

    @Query("""
        UPDATE app_use_environment
        SET deleted_at = :deletedAt
        WHERE member_id = :memberId
            AND deleted_at IS NULL
    """)
    fun deleteAllByMemberIdAndDeletedAtIsNull(
        memberId: Long,
        deletedAt: LocalDateTime,
    ): Mono<Void>
}
