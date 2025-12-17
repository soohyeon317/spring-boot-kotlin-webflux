package com.example.springbootkotlinwebflux.persistence.jpa.account

import com.example.springbootkotlinwebflux.domain.account.Account
import com.example.springbootkotlinwebflux.domain.account.ThirdPartyAuthType
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("account")
data class AccountEntity (
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

    constructor(account: Account, willDelete: Boolean = false) :
            this(
                id = account.id,
                thirdPartyAuthType = account.thirdPartyAuthType,
                thirdPartyAuthUid = account.thirdPartyAuthUid,
                email = account.email,
                languageCode = account.languageCode,
                countryCode = account.countryCode,
                timeZoneCode = account.timeZoneCode,
                createdAt = if (account.id == null) {
                    LocalDateTime.now()
                } else {
                    account.createdAt ?: LocalDateTime.now()
                },
                updatedAt = if (account.id != null) {
                    if (willDelete) {
                        account.updatedAt
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

    fun toAccount(): Account = Account(
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
