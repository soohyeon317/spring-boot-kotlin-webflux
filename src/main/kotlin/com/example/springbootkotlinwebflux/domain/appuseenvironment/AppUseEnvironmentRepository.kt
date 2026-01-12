package com.example.springbootkotlinwebflux.domain.appuseenvironment

import java.time.LocalDateTime

interface AppUseEnvironmentRepository {

    suspend fun save(
        appUseEnvironment: AppUseEnvironment,
        willDelete: Boolean = false
    ): AppUseEnvironment

    suspend fun findTopByMemberIdAndDeviceModelNameAndDeletedAtIsNullOrderByIdDesc(
        memberId: Long,
        deviceModelName: String
    ): AppUseEnvironment?

    suspend fun deleteAllByMemberIdAndDeviceModelNameAndDeletedAtIsNull(
        memberId: Long,
        deviceModelName: String,
        deletedAt: LocalDateTime
    )

    suspend fun deleteAllByMemberIdAndDeletedAtIsNull(
        memberId: Long,
        deletedAt: LocalDateTime
    )
}
