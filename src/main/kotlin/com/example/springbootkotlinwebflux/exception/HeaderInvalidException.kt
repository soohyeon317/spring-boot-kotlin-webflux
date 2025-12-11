package com.example.springbootkotlinwebflux.exception

class HeaderInvalidException : BadRequestException {
    constructor(code: ErrorCode, message: String?) : super(code, message)
    constructor(code: ErrorCode) : this(code, code.message)
}
