package com.example.springbootkotlinwebflux.infrastructure.persistence.jpa.authtoken

import com.example.springbootkotlinwebflux.domain.authtoken.AuthToken
import com.example.springbootkotlinwebflux.domain.authtoken.AuthTokenRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class AuthTokenRepositoryImpl(
    private val springDataAuthTokenRepository: SpringDataAuthTokenRepository,
    private val ioDispatcher: CoroutineDispatcher
) : AuthTokenRepository {

    override suspend fun save(authToken: AuthToken, willDelete: Boolean): AuthToken = withContext(ioDispatcher) {
        springDataAuthTokenRepository.save(
            AuthTokenEntity(authToken, willDelete)
        ).awaitSingle().toAuthToken()
    }

    override suspend fun findTopByAccessTokenAndDeletedAtIsNullOrderByIdDesc(accessToken: String): AuthToken? = withContext(ioDispatcher) {
        springDataAuthTokenRepository.findTopByAccessTokenAndDeletedAtIsNullOrderByIdDesc(accessToken)
            .awaitSingleOrNull()?.toAuthToken()
    }

    override suspend fun findTopByAccountIdAndAccessTokenAndDeletedAtIsNullOrderByIdDesc(accountId: Long, accessToken: String): AuthToken? = withContext(ioDispatcher) {
        springDataAuthTokenRepository.findTopByAccountIdAndAccessTokenAndDeletedAtIsNullOrderByIdDesc(
            accountId,
            accessToken
        ).awaitSingleOrNull()?.toAuthToken()
    }

    override suspend fun deleteAllByAccountIdAndDeletedAtIsNull(
        accountId: Long,
        deletedAt: LocalDateTime,
    ): Unit = withContext(ioDispatcher) {
        springDataAuthTokenRepository.deleteAllByAccountIdAndDeletedAtIsNull(
            accountId = accountId,
            deletedAt = deletedAt
        ).awaitSingleOrNull()
    }
}
