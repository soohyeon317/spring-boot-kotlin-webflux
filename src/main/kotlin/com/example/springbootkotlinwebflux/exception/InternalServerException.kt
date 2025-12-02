package com.example.springbootkotlinwebflux.exception

open class InternalServerException(code: ErrorCode, message: String?): ServiceRuntimeException(code, message)
