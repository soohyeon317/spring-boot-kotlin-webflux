package com.example.springbootkotlinwebflux.exception

open class ResourceNotFoundException(code: ErrorCode, message: String?): ServiceRuntimeException(code, message)
