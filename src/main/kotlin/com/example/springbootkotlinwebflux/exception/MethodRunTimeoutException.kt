package com.example.springbootkotlinwebflux.exception

class MethodRunTimeoutException : InternalServerException {
    constructor(code: ErrorCode, message: String?) : super(code, message)
    constructor(code: ErrorCode) : this(code, code.message)
}
