package com.example.springbootkotlinwebflux.application.account

class AccountWithdrawCommand {

    data class Withdraw(val accountId: Long)
}
