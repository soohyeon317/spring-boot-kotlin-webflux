package com.example.springbootkotlinwebflux.exception

open class ConflictException(code: ErrorCode, message: String?): ServiceRuntimeException(code, message)
