package com.example.springbootkotlinwebflux.infrastructure.persistence.jpa.appuseenvironment

import com.example.springbootkotlinwebflux.domain.appuseenvironment.AppUseEnvironment
import com.example.springbootkotlinwebflux.domain.appuseenvironment.AppUseEnvironmentRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class AppUseEnvironmentRepositoryImpl(
    private val springDataAppUseEnvironmentRepository: SpringDataAppUseEnvironmentRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : AppUseEnvironmentRepository {

    override suspend fun save(
        appUseEnvironment: AppUseEnvironment,
        willDelete: Boolean,
    ): AppUseEnvironment = withContext(ioDispatcher) {
        springDataAppUseEnvironmentRepository.save(
            AppUseEnvironmentEntity(
                appUseEnvironment = appUseEnvironment,
                willDelete = willDelete
            )
        ).awaitSingle().toAppUseEnvironment()
    }

    override suspend fun findTopByMemberIdAndDeviceModelNameAndDeletedAtIsNullOrderByIdDesc(
        memberId: Long,
        deviceModelName: String,
    ): AppUseEnvironment? = withContext(ioDispatcher) {
        springDataAppUseEnvironmentRepository.findTopByMemberIdAndDeviceModelNameAndDeletedAtIsNullOrderByIdDesc(
            memberId = memberId,
            deviceModelName = deviceModelName
        ).awaitSingleOrNull()?.toAppUseEnvironment()
    }

    override suspend fun deleteAllByMemberIdAndDeviceModelNameAndDeletedAtIsNull(
        memberId: Long,
        deviceModelName: String,
        deletedAt: LocalDateTime,
    ): Unit = withContext(ioDispatcher) {
        springDataAppUseEnvironmentRepository.deleteAllByMemberIdAndDeviceModelNameAndDeletedAtIsNull(
            memberId = memberId,
            deviceModelName = deviceModelName,
            deletedAt = deletedAt
        ).awaitSingleOrNull()
    }

    override suspend fun deleteAllByMemberIdAndDeletedAtIsNull(
        memberId: Long,
        deletedAt: LocalDateTime
    ): Unit = withContext(ioDispatcher) {
        springDataAppUseEnvironmentRepository.deleteAllByMemberIdAndDeletedAtIsNull(
            memberId = memberId,
            deletedAt = deletedAt
        ).awaitSingleOrNull()
    }


}
