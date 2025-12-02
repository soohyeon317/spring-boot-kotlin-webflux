package com.example.springbootkotlinwebflux.infrastructure.persistence.jpa.account

import com.example.springbootkotlinwebflux.domain.account.ThirdPartyAuthType
import com.example.springbootkotlinwebflux.persistence.jpa.account.AccountEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono

interface SpringDataAccountRepository : R2dbcRepository<AccountEntity, Long> {

    fun findTopByThirdPartyAuthTypeAndThirdPartyAuthUidAndDeletedAtIsNullOrderByIdDesc(
        thirdPartyAuthType: ThirdPartyAuthType,
        thirdPartyAuthUid: String
    ): Mono<AccountEntity>

    fun findTopByIdAndDeletedAtIsNull(id: Long): Mono<AccountEntity>

    @Query("""
        UPDATE account AS ACC
        SET ACC.language_code = :languageCode
            ,ACC.country_code = :countryCode
            ,ACC.time_zone_code = :timeZoneCode
            ,ACC.updated_at = now()
        WHERE ACC.id = :accountId
    """)
    fun updateAccountLocale(
        accountId: Long,
        languageCode: String,
        countryCode: String,
        timeZoneCode: String
    ): Mono<Void>
}
