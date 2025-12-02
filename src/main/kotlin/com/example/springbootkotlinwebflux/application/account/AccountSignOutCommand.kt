package com.example.springbootkotlinwebflux.application.account

class AccountSignOutCommand {

    data class SignOut(val accountId: Long, val accessToken: String)
}
