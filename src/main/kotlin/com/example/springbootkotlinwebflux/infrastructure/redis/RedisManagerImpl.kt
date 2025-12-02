package com.example.springbootkotlinwebflux.infrastructure.redis

import com.example.springbootkotlinwebflux.configuration.redis.RedisManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.redisson.api.RedissonReactiveClient
import org.springframework.stereotype.Component

@Component
class RedisManagerImpl(
    private val redissonReactiveClient: RedissonReactiveClient,
    private val ioDispatcher: CoroutineDispatcher
): RedisManager {

    override suspend fun rollbackForStringValue(previousKeyValues: Map<String, String?>) =
        withContext(ioDispatcher) {
            for (previousKeyValue in previousKeyValues.entries) {
                if (previousKeyValue.value == null) {
                    deleteKeyForStringValue(previousKeyValue.key)
                } else {
                    val bucket = redissonReactiveClient.getBucket<String>(previousKeyValue.key)
                    val hasKey = bucket.isExists.awaitSingle()
                    val setter = if (!hasKey) {
                        bucket.setIfAbsent(previousKeyValue.value)
                    } else {
                        bucket.setIfExists(previousKeyValue.value)
                    }
                    setter.awaitSingle()
                }
            }
        }

    override suspend fun rollbackForListValue(previousKeyValues: Map<String, List<String>?>) =
        withContext(ioDispatcher) {
            for (previousKeyValue in previousKeyValues.entries) {
                deleteKeyForListValue(previousKeyValue.key)
                val values = previousKeyValue.value
                if (values != null) {
                    val list = redissonReactiveClient.getList<String>(previousKeyValue.key)
                    for (value in values) {
                        list.add(value).awaitSingle()
                    }
                }
            }
        }

    override suspend fun deleteKeyForListValue(key: String): Boolean =
        withContext(ioDispatcher) {
            val bucket = redissonReactiveClient.getList<String>(key)
            val hasBucket = bucket.isExists.awaitSingle()
            if (!hasBucket) {
                true
            } else {
                bucket.delete().awaitSingle()
            }
        }

    private suspend fun deleteKeyForStringValue(key: String): Boolean =
        withContext(ioDispatcher) {
            val bucket = redissonReactiveClient.getBucket<String>(key)
            val hasBucket = bucket.isExists.awaitSingle()
            if (!hasBucket) {
                true
            } else {
                bucket.delete().awaitSingle()
            }
        }
}
