package com.example.springbootkotlinwebflux.application.account

import com.example.springbootkotlinwebflux.domain.account.AccountForResponse

fun interface AccountDetailsGetUseCase {

    suspend fun getAccountDetails(command: AccountDetailsGetCommand.GetAccountDetails): AccountForResponse
}
