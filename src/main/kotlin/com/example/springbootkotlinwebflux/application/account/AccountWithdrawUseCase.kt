package com.example.springbootkotlinwebflux.application.account

fun interface AccountWithdrawUseCase {

    suspend fun withdraw(command: AccountWithdrawCommand.Withdraw)
}
