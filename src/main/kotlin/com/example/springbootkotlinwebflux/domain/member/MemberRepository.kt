package com.example.springbootkotlinwebflux.domain.member

interface MemberRepository {

    suspend fun save(member: Member, willDelete: Boolean = false): Member
    suspend fun findTopByThirdPartyAuthTypeAndThirdPartyAuthUidAndDeletedAtIsNullOrderByIdDesc(
        thirdPartyAuthType: ThirdPartyAuthType,
        thirdPartyAuthUid: String
    ): Member?
    suspend fun findTopByIdAndDeletedAtIsNull(id: Long): Member?
    suspend fun updateMemberLocale(
        id: Long,
        languageCode: String,
        countryCode: String,
        timeZoneCode: String
    )
}
