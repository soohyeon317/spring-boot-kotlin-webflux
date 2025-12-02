package com.example.springbootkotlinwebflux.application.account

class AccountDetailGetCommand {

    data class GetAccountDetail(val accountId: Long,
                                val languageCode: String,
                                val countryCode: String,
                                val timeZoneCode: String)
}
