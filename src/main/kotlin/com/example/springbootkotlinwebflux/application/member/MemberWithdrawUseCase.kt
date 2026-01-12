package com.example.springbootkotlinwebflux.application.member

fun interface MemberWithdrawUseCase {

    suspend fun withdraw(command: MemberWithdrawCommand.Withdraw)
}
