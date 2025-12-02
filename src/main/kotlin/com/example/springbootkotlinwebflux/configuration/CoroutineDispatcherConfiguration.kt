package com.example.springbootkotlinwebflux.configuration

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoroutineDispatcherConfiguration {

    @Bean
    fun ioDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Bean
    fun defaultDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }
}
