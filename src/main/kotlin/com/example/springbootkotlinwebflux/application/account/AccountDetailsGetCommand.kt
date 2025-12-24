package com.example.springbootkotlinwebflux.application.account

class AccountDetailsGetCommand {

    data class GetAccountDetails(val accountId: Long,
                                 val languageCode: String,
                                 val countryCode: String,
                                 val timeZoneCode: String)
}
