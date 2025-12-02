package com.example.springbootkotlinwebflux.persistence.jpa.account

import com.example.springbootkotlinwebflux.domain.account.Account
import com.example.springbootkotlinwebflux.domain.account.AccountRepository
import com.example.springbootkotlinwebflux.domain.account.ThirdPartyAuthType
import com.example.springbootkotlinwebflux.infrastructure.persistence.jpa.account.SpringDataAccountRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Repository

@Repository
class AccountRepositoryImpl(
    private val springDataAccountRepository: SpringDataAccountRepository,
    private val ioDispatcher: CoroutineDispatcher
) : AccountRepository {

    override suspend fun save(account: Account, willDelete: Boolean?): Account = withContext(ioDispatcher) {
        springDataAccountRepository.save(
            AccountEntity(account, willDelete)
        ).awaitSingle().toAccount()
    }

    override suspend fun findTopByThirdPartyAuthTypeAndThirdPartyAuthUidAndDeletedAtIsNullOrderByIdDesc(
        thirdPartyAuthType: ThirdPartyAuthType,
        thirdPartyAuthUid: String
    ): Account? = withContext(ioDispatcher) {
        springDataAccountRepository.findTopByThirdPartyAuthTypeAndThirdPartyAuthUidAndDeletedAtIsNullOrderByIdDesc(
            thirdPartyAuthType,
            thirdPartyAuthUid
        ).awaitSingleOrNull()?.toAccount()
    }

    override suspend fun findTopByIdAndDeletedAtIsNull(id: Long): Account? = withContext(ioDispatcher) {
        springDataAccountRepository.findTopByIdAndDeletedAtIsNull(id).awaitSingleOrNull()?.toAccount()
    }

    override suspend fun updateAccountLocale(
        id: Long,
        languageCode: String,
        countryCode: String,
        timeZoneCode: String
    ): Unit = withContext(ioDispatcher) {
        springDataAccountRepository.updateAccountLocale(
            accountId = id,
            languageCode = languageCode,
            countryCode = countryCode,
            timeZoneCode = timeZoneCode
        ).awaitSingleOrNull()
    }
}
