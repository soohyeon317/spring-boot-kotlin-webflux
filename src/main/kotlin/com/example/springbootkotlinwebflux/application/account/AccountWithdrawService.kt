package com.example.springbootkotlinwebflux.application.account

import com.example.springbootkotlinwebflux.configuration.logger.logger
import com.example.springbootkotlinwebflux.domain.account.AccountRepository
import com.example.springbootkotlinwebflux.domain.authtoken.AuthTokenRepository
import com.example.springbootkotlinwebflux.exception.AccountNotFoundException
import com.example.springbootkotlinwebflux.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountWithdrawService(
    private val accountRepository: AccountRepository,
    private val authTokenRepository: AuthTokenRepository,
): AccountWithdrawUseCase {

    @Transactional(rollbackFor = [Throwable::class])
    override suspend fun withdraw(command: AccountWithdrawCommand.Withdraw) {
        try {
            val myAccount = accountRepository.findTopByIdAndDeletedAtIsNull(command.accountId)
                ?: throw AccountNotFoundException(ErrorCode.MY_ACCOUNT_NOT_FOUND)

            // 삭제되지 않은 액세스 토큰 목록 삭제
            deleteAuthTokens(command.accountId)

            // 탈퇴 계정 삭제
            accountRepository.save(
                account = myAccount,
                willDelete = true
            )
        } catch (e: Throwable) {
            logger().error("command=$command", e)
            throw e
        }
    }

    private suspend fun deleteAuthTokens(accountId: Long) {
        val authTokens = authTokenRepository.findAllByAccountIdAndDeletedAtIsNull(accountId)
        for (authToken in authTokens) {
            authTokenRepository.save(authToken, true)
        }
    }
}
