package com.example.springbootkotlinwebflux.infrastructure.persistence.jpa.member

import com.example.springbootkotlinwebflux.domain.member.Member
import com.example.springbootkotlinwebflux.domain.member.ThirdPartyAuthType
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("member")
data class MemberEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long?,
    @Enumerated(EnumType.STRING) val thirdPartyAuthType: ThirdPartyAuthType,
    val thirdPartyAuthUid: String,
    val email: String,
    val languageCode: String,
    val countryCode: String,
    val timeZoneCode: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
) {

    constructor(member: Member, willDelete: Boolean = false) :
            this(
                id = member.id,
                thirdPartyAuthType = member.thirdPartyAuthType,
                thirdPartyAuthUid = member.thirdPartyAuthUid,
                email = member.email,
                languageCode = member.languageCode,
                countryCode = member.countryCode,
                timeZoneCode = member.timeZoneCode,
                createdAt = if (member.id == null) {
                    LocalDateTime.now()
                } else {
                    member.createdAt ?: LocalDateTime.now()
                },
                updatedAt = if (member.id != null) {
                    if (willDelete) {
                        member.updatedAt
                    } else {
                        LocalDateTime.now()
                    }
                } else {
                    null
                },
                deletedAt = if (willDelete) {
                    LocalDateTime.now()
                } else {
                    null
                }
            )

    fun toMember(): Member = Member(
        id = this.id,
        thirdPartyAuthType = this.thirdPartyAuthType,
        thirdPartyAuthUid = this.thirdPartyAuthUid,
        email = this.email,
        languageCode = this.languageCode,
        countryCode = this.countryCode,
        timeZoneCode = this.timeZoneCode,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = this.deletedAt
    )
}
