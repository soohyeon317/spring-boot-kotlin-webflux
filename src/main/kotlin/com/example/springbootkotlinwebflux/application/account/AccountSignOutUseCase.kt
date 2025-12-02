package com.example.springbootkotlinwebflux.application.account

fun interface AccountSignOutUseCase {

    suspend fun signOut(command: AccountSignOutCommand.SignOut)
}
