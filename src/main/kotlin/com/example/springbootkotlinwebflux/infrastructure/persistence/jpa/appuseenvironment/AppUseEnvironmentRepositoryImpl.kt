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

    override suspend fun findTopByAccountIdAndDeviceModelNameAndDeletedAtIsNullOrderByIdDesc(
        accountId: Long,
        deviceModelName: String,
    ): AppUseEnvironment? = withContext(ioDispatcher) {
        springDataAppUseEnvironmentRepository.findTopByAccountIdAndDeviceModelNameAndDeletedAtIsNullOrderByIdDesc(
            accountId = accountId,
            deviceModelName = deviceModelName
        ).awaitSingleOrNull()?.toAppUseEnvironment()
    }

    override suspend fun deleteAllByAccountIdAndDeviceModelNameAndDeletedAtIsNull(
        accountId: Long,
        deviceModelName: String,
        deletedAt: LocalDateTime,
    ): Unit = withContext(ioDispatcher) {
        springDataAppUseEnvironmentRepository.deleteAllByAccountIdAndDeviceModelNameAndDeletedAtIsNull(
            accountId = accountId,
            deviceModelName = deviceModelName,
            deletedAt = deletedAt
        ).awaitSingleOrNull()
    }

    override suspend fun deleteAllByAccountIdAndDeletedAtIsNull(
        accountId: Long,
        deletedAt: LocalDateTime
    ): Unit = withContext(ioDispatcher) {
        springDataAppUseEnvironmentRepository.deleteAllByAccountIdAndDeletedAtIsNull(
            accountId = accountId,
            deletedAt = deletedAt
        ).awaitSingleOrNull()
    }


}
