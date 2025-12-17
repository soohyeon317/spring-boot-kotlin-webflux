package com.example.springbootkotlinwebflux.domain.appuseenvironment

import java.time.LocalDateTime

interface AppUseEnvironmentRepository {

    suspend fun save(
        appUseEnvironment: AppUseEnvironment,
        willDelete: Boolean = false
    ): AppUseEnvironment

    suspend fun findTopByAccountIdAndDeviceModelNameAndDeletedAtIsNullOrderByIdDesc(
        accountId: Long,
        deviceModelName: String
    ): AppUseEnvironment?

    suspend fun deleteAllByAccountIdAndDeviceModelNameAndDeletedAtIsNull(
        accountId: Long,
        deviceModelName: String,
        deletedAt: LocalDateTime
    )

    suspend fun deleteAllByAccountIdAndDeletedAtIsNull(
        accountId: Long,
        deletedAt: LocalDateTime
    )
}
