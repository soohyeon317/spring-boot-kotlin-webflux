package com.example.springbootkotlinwebflux

import org.redisson.spring.starter.RedissonAutoConfigurationV2
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.web.reactive.config.EnableWebFlux
import reactor.blockhound.BlockHound
import reactor.core.publisher.Hooks
import java.util.*

@EnableWebFlux
@EnableR2dbcRepositories
@SpringBootApplication(exclude = [RedissonAutoConfigurationV2::class])
@ConfigurationPropertiesScan
class SpringBootKotlinWebfluxApplication

fun main(args: Array<String>) {
    val props: Properties = System.getProperties()
    if (props["blockhound"] == "on") {
        println("Block hound installed.")
        BlockHound.install({
            it.allowBlockingCallsInside(
                "org.springframework.validation.beanvalidation.SpringValidatorAdapter",
                "validate"
            )
            it.allowBlockingCallsInside(
                "org.springframework.aop.framework.CglibAopProxy\$DynamicAdvisedInterceptor",
                "intercept"
            )
            it.allowBlockingCallsInside( // for springdoc-openapi
                "java.util.zip.InflaterInputStream",
                "read"
            )
        })
    }

    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    runApplication<SpringBootKotlinWebfluxApplication>(*args)
    Hooks.enableAutomaticContextPropagation() // 로그에 MDC 요청 추적 ID 출력을 위해 활성화. ThreadLocal 값이 자동으로 Context에 전파됨.
}
