package com.example.springbootkotlinwebflux.domain.common

enum class ServerReleaseEnvironment(val code: String) {

    LOCAL("local"),
    DEV("dev"),
    PROD("prod")
    ;

    companion object {

        fun fromCode(code: String): ServerReleaseEnvironment =
            entries.firstOrNull { it.code == code }
                ?: throw IllegalArgumentException("Unknown value: $code")
    }
}
