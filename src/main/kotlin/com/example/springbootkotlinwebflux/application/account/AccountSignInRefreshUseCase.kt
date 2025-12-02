package com.example.springbootkotlinwebflux.application.account

import com.example.springbootkotlinwebflux.domain.authtoken.AuthToken

fun interface AccountSignInRefreshUseCase {

    suspend fun refreshSignIn(command: AccountSignInRefreshCommand.RefreshSignIn): AuthToken
}
