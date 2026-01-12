package com.example.springbootkotlinwebflux.application.member

fun interface MemberSignOutUseCase {

    suspend fun signOut(command: MemberSignOutCommand.SignOut)
}
