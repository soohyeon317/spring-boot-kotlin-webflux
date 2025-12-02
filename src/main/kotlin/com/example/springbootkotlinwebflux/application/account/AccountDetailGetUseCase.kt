package com.example.springbootkotlinwebflux.application.account

import com.example.springbootkotlinwebflux.domain.account.AccountForResponse

fun interface AccountDetailGetUseCase {

    suspend fun getAccountDetail(command: AccountDetailGetCommand.GetAccountDetail): AccountForResponse
}
