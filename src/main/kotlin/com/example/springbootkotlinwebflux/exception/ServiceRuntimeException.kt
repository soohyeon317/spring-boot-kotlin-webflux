package com.example.springbootkotlinwebflux.exception

open class ServiceRuntimeException(open var code: ErrorCode, override var message: String?) : RuntimeException()
