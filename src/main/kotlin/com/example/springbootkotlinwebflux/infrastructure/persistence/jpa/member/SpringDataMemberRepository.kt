package com.example.springbootkotlinwebflux.infrastructure.persistence.jpa.member

import com.example.springbootkotlinwebflux.domain.member.ThirdPartyAuthType
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono

interface SpringDataMemberRepository : R2dbcRepository<MemberEntity, Long> {

    fun findTopByThirdPartyAuthTypeAndThirdPartyAuthUidAndDeletedAtIsNullOrderByIdDesc(
        thirdPartyAuthType: ThirdPartyAuthType,
        thirdPartyAuthUid: String
    ): Mono<MemberEntity>

    fun findTopByIdAndDeletedAtIsNull(id: Long): Mono<MemberEntity>

    @Query("""
        UPDATE member AS MB
        SET MB.language_code = :languageCode
            ,MB.country_code = :countryCode
            ,MB.time_zone_code = :timeZoneCode
            ,MB.updated_at = now()
        WHERE MB.id = :id
    """)
    fun updateMemberLocale(
        id: Long,
        languageCode: String,
        countryCode: String,
        timeZoneCode: String
    ): Mono<Void>
}
