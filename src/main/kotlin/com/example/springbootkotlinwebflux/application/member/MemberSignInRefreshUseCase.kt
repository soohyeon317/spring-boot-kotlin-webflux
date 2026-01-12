package com.example.springbootkotlinwebflux.application.member

import com.example.springbootkotlinwebflux.domain.authtoken.AuthToken

fun interface MemberSignInRefreshUseCase {

    suspend fun refreshSignIn(command: MemberSignInRefreshCommand.RefreshSignIn): AuthToken
}
