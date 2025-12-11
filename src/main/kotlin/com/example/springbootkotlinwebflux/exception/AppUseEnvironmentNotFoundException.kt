package com.example.springbootkotlinwebflux.exception

class AppUseEnvironmentNotFoundException : ResourceNotFoundException {
    constructor(code: ErrorCode, message: String?) : super(code, message)
    constructor(code: ErrorCode) : this(code, code.message)
}
