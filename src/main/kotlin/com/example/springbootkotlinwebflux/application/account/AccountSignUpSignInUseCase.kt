package com.example.springbootkotlinwebflux.application.account

import com.example.springbootkotlinwebflux.domain.authtoken.AuthToken

fun interface AccountSignUpSignInUseCase {

    suspend fun signUpSignIn(command: AccountSignUpSignInCommand.SignUpSignIn): Pair<AuthToken, Boolean>
}
