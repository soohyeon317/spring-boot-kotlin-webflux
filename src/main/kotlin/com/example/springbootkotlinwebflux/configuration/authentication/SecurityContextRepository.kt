package com.example.springbootkotlinwebflux.configuration.authentication

import com.example.springbootkotlinwebflux.exception.ErrorCode
import com.example.springbootkotlinwebflux.exception.UnAuthorizedException
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(
    val authenticationManager: AuthenticationManager,
) : ServerSecurityContextRepository {

    override fun save(
        exchange: ServerWebExchange,
        context: SecurityContext?,
    ): Mono<Void> {
        return Mono.empty()
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        val authorization = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        return Mono.justOrEmpty(authorization)
            .filter { auth ->
                auth.startsWith(AuthenticationToken.BEARER_TOKEN_PREFIX, true)
            }
            .flatMap { auth ->
                val isBearerToken = auth.startsWith(AuthenticationToken.BEARER_TOKEN_PREFIX, true)
                val accessToken: String
                val authentication: Authentication
                if (isBearerToken) {
                    accessToken = auth.substring(AuthenticationToken.BEARER_TOKEN_PREFIX.length)
                    authentication = UsernamePasswordAuthenticationToken(accessToken, accessToken, emptyList())
                    authenticationManager.authenticate(authentication).map { s -> SecurityContextImpl(s) }
                } else {
                    return@flatMap Mono.error(UnAuthorizedException(ErrorCode.UNAUTHORIZED))
                }
            }
    }
}
