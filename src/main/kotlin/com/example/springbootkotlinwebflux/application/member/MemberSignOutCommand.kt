package com.example.springbootkotlinwebflux.application.member

class MemberSignOutCommand {

    data class SignOut(
        val memberId: Long,
        val accessToken: String,
        val deviceModelName: String,
    )
}
