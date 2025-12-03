package com.example.springbootkotlinwebflux.exception

enum class ErrorCode(val message: String) {

    // INPUT
    INPUT_INVALID("Input is invalid."),

    // AUTHENTICATION
    UNAUTHORIZED("Account is unauthorized."),
    ACCESS_FORBIDDEN("Access is forbidden."),
    ACCESS_TOKEN_EXPIRED("AccessToken is expired."),
    REFRESH_TOKEN_EXPIRED("RefreshToken is expired."),
    ACCESS_TOKEN_INVALID("AccessToken is invalid."),
    REFRESH_TOKEN_INVALID("RefreshToken is invalid."),

    // ACCOUNT
    MY_ACCOUNT_NOT_FOUND("My account is not found."),

    // AUTH_TOKEN
    ACCESS_TOKEN_NOT_FOUND("AccessToken is not found."),
    REFRESH_TOKEN_NOT_MATCHED("RefreshToken is not matched."),

    // INTERNAL SERVER ERROR
    DISTRIBUTED_LOCK_ACQUISITION_FAILURE("Fail to acquire the lock."),
    METHOD_RUN_TIMEOUT("Method run timeout occurred."),
}
