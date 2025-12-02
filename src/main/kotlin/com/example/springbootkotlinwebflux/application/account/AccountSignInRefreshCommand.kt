package com.example.springbootkotlinwebflux.application.account

class AccountSignInRefreshCommand {

    data class RefreshSignIn(
        val accessToken: String,
        val refreshToken: String,
    )
}
