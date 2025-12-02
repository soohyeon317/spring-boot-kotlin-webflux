package com.example.springbootkotlinwebflux.exception

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.webflux.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.MissingRequestValueException

@Component
class GlobalErrorAttributes: DefaultErrorAttributes() {

    override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions)
    : Map<String, Any?> {

        val errorMap = mutableMapOf<String, Any?>()
        when (val throwable: Throwable = getError(request)) {
            is MissingRequestValueException -> {
                errorMap["status"] = HttpStatus.BAD_REQUEST.value()
                errorMap["code"] = ErrorCode.INPUT_INVALID
                var errorContent = ErrorCode.INPUT_INVALID.message
                throwable.reason?.let {
                    errorContent = it
                }
                errorMap["message"] = errorContent
            }
            is HandlerMethodValidationException -> {
                var errorContent = ErrorCode.INPUT_INVALID.message
                throwable.allErrors.forEach { error ->
                    errorContent = (error.codes?.get(1) ?: error.codes?.firstOrNull()) + ":" + error.defaultMessage
                }
                errorMap["status"] = HttpStatus.BAD_REQUEST.value()
                errorMap["code"] = ErrorCode.INPUT_INVALID
                errorMap["message"] = errorContent
            }
            is WebExchangeBindException -> {
                var errorContent = ErrorCode.INPUT_INVALID.message
                throwable.fieldError?.let {
                    errorContent = it.field + ":" + it.defaultMessage
                }
                errorMap["status"] = HttpStatus.BAD_REQUEST.value()
                errorMap["code"] = ErrorCode.INPUT_INVALID
                errorMap["message"] = errorContent
            }
            is UnAuthorizedException -> {
                errorMap["status"] = HttpStatus.UNAUTHORIZED.value()
                errorMap["code"] = throwable.code
                errorMap["message"] = throwable.message
            }
            is AccessForbiddenException -> {
                errorMap["status"] = HttpStatus.FORBIDDEN.value()
                errorMap["code"] = throwable.code
                errorMap["message"] = throwable.message
            }
            is ResourceNotFoundException -> {
                errorMap["status"] = HttpStatus.NOT_FOUND.value()
                errorMap["code"] = throwable.code
                errorMap["message"] = throwable.message
            }
            is BadRequestException -> {
                errorMap["status"] = HttpStatus.BAD_REQUEST.value()
                errorMap["code"] = throwable.code
                errorMap["message"] = throwable.message
            }
            is ConflictException -> {
                errorMap["status"] = HttpStatus.CONFLICT.value()
                errorMap["code"] = throwable.code
                errorMap["message"] = throwable.message
            }
            is InternalServerException -> {
                errorMap["status"] = HttpStatus.INTERNAL_SERVER_ERROR.value()
                errorMap["code"] = throwable.code
                errorMap["message"] = throwable.message
            }
            else -> {
                return super.getErrorAttributes(request, options)
            }
        }
        return errorMap
    }
}
