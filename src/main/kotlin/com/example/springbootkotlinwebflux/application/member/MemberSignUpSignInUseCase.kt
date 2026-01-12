package com.example.springbootkotlinwebflux.application.member

import com.example.springbootkotlinwebflux.domain.authtoken.AuthToken

fun interface MemberSignUpSignInUseCase {

    suspend fun signUpSignIn(command: MemberSignUpSignInCommand.SignUpSignIn): Pair<AuthToken, Boolean>
}
