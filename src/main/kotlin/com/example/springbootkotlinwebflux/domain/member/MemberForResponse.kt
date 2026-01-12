package com.example.springbootkotlinwebflux.domain.member

import kotlinx.serialization.Serializable

@Serializable
data class MemberForResponse(
    val memberId: Long,
    val thirdPartyAuthType: ThirdPartyAuthType,
    val thirdPartyAuthUid: String,
    val email: String,
    val languageCode: String,
    val countryCode: String,
    val timeZoneCode: String,
    val createdAt: String,
)
