package com.example.springbootkotlinwebflux.domain.account

interface AccountRepository {

    suspend fun save(account: Account, willDelete: Boolean? = null): Account
    suspend fun findTopByThirdPartyAuthTypeAndThirdPartyAuthUidAndDeletedAtIsNullOrderByIdDesc(
        thirdPartyAuthType: ThirdPartyAuthType,
        thirdPartyAuthUid: String
    ): Account?
    suspend fun findTopByIdAndDeletedAtIsNull(id: Long): Account?
    suspend fun updateAccountLocale(
        id: Long,
        languageCode: String,
        countryCode: String,
        timeZoneCode: String
    )
}
