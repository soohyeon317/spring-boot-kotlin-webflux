package com.example.springbootkotlinwebflux.application.member

import com.example.springbootkotlinwebflux.domain.member.MemberForResponse

fun interface MemberDetailsGetUseCase {

    suspend fun getMemberDetails(command: MemberDetailsGetCommand.GetMemberDetails): MemberForResponse
}
