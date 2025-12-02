package com.example.springbootkotlinwebflux.configuration

import com.example.springbootkotlinwebflux.configuration.authentication.AuthenticationManager
import com.example.springbootkotlinwebflux.exception.AccessForbiddenException
import com.example.springbootkotlinwebflux.exception.ErrorCode
import com.example.springbootkotlinwebflux.exception.UnAuthorizedException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class WebSecurityConfiguration(
    val authenticationManager: AuthenticationManager,
    val securityContextRepository: ServerSecurityContextRepository
) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
        http
            .cors {  }
            .csrf {
                it.disable()
            }
            .httpBasic {
                it.disable()
            }
            .formLogin {
                it.disable()
            }
            .authorizeExchange {
                val authenticatedUrls = listOf(
                    // accounts
                    "/api/v1/accounts/sign-up/sign-in",
                    "/api/v1/accounts/sign-in/refresh",

                    // Health Check
                    "/health"
                )
                it.pathMatchers(
                    *authenticatedUrls.toTypedArray()
                ).permitAll().anyExchange().authenticated()
            }
            .securityContextRepository(securityContextRepository)
            .authenticationManager(authenticationManager)
            .exceptionHandling {
                it.authenticationEntryPoint { _, e ->
                    Mono.error(UnAuthorizedException(ErrorCode.UNAUTHORIZED, e.message))
                }
                it.accessDeniedHandler { _, e ->
                    Mono.error(AccessForbiddenException(ErrorCode.ACCESS_FORBIDDEN, e.message))
                }
            }
            .build()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOriginPatterns = listOf(
            "*"
        )
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE")         // 허용할 HTTP Method
        configuration.allowedHeaders = listOf("*")                   // 허용할 Header
        configuration.allowCredentials = true                        // 인증정보 허용 여부

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)       // 모든 경로에 적용
        return source
    }
}
