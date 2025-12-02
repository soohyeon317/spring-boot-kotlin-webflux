package com.example.springbootkotlinwebflux.configuration.redis

interface RedisManager {

    suspend fun rollbackForStringValue(previousKeyValues: Map<String, String?>)
    suspend fun rollbackForListValue(previousKeyValues: Map<String, List<String>?>)
    suspend fun deleteKeyForListValue(key: String): Boolean
}
