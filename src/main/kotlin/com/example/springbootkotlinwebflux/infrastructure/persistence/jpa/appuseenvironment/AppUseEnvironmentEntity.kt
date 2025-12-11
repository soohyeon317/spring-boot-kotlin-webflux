package com.example.springbootkotlinwebflux.infrastructure.persistence.jpa.appuseenvironment

import com.example.springbootkotlinwebflux.domain.appuseenvironment.AppOS
import com.example.springbootkotlinwebflux.domain.appuseenvironment.AppUseEnvironment
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(name = "app_use_environment")
data class AppUseEnvironmentEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?,
    val accountId: Long,
    val deviceModelName: String,
    @Enumerated(EnumType.STRING) val appOs: AppOS,
    val appVersion: String,
    val appPushToken: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
) {

    constructor(appUseEnvironment: AppUseEnvironment, willDelete: Boolean? = null) :
            this(
                id = appUseEnvironment.id,
                accountId = appUseEnvironment.accountId,
                deviceModelName = appUseEnvironment.deviceModelName,
                appVersion = appUseEnvironment.appVersion,
                appOs = appUseEnvironment.appOs,
                appPushToken = appUseEnvironment.appPushToken,
                createdAt = if (appUseEnvironment.id == null) {
                    LocalDateTime.now()
                } else {
                    appUseEnvironment.createdAt ?: LocalDateTime.now()
                },
                updatedAt = if (appUseEnvironment.id != null) {
                    if (willDelete == true) {
                        appUseEnvironment.updatedAt
                    } else {
                        LocalDateTime.now()
                    }
                } else {
                    null
                },
                deletedAt = if (willDelete != null && willDelete == true) {
                    LocalDateTime.now()
                } else {
                    null
                }
            )

    fun toAppUseEnvironment(): AppUseEnvironment = AppUseEnvironment(
        id = this.id,
        accountId = this.accountId,
        deviceModelName = this.deviceModelName,
        appOs = this.appOs,
        appVersion = this.appVersion,
        appPushToken = this.appPushToken,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}
