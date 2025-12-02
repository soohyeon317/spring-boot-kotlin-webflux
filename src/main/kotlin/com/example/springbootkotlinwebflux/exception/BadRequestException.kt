package com.example.springbootkotlinwebflux.exception

open class BadRequestException(code: ErrorCode, message: String?): ServiceRuntimeException(code, message)
