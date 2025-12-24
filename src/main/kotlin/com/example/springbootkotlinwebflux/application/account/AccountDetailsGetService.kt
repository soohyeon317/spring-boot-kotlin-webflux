package com.example.springbootkotlinwebflux.application.account

import com.example.springbootkotlinwebflux.configuration.logger.logger
import com.example.springbootkotlinwebflux.domain.account.AccountForResponse
import com.example.springbootkotlinwebflux.domain.account.AccountRepository
import com.example.springbootkotlinwebflux.exception.AccountNotFoundException
import com.example.springbootkotlinwebflux.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountDetailsGetService(
    private val accountRepository: AccountRepository,
): AccountDetailsGetUseCase {

    @Transactional(rollbackFor = [Throwable::class])
    override suspend fun getAccountDetails(command: AccountDetailsGetCommand.GetAccountDetails): AccountForResponse {
        try {
            // 기존 계정 데이터 조회
            var myAccount = accountRepository.findTopByIdAndDeletedAtIsNull(id = command.accountId)
                ?: throw AccountNotFoundException(ErrorCode.MY_ACCOUNT_NOT_FOUND)

            // 기존 Locale 정보가 최신 정보와 다르면, 업데이트
            if (!myAccount.isLatestLocaleInfo(
                    latestLanguageCode = command.languageCode,
                    latestCountryCode = command.countryCode,
                    latestTimeZoneCode = command.timeZoneCode
                )) {
                // 언어/국가/타임존 정보 업데이트
                val willUpdateLanguageCode = (myAccount.languageCode != command.languageCode)
                val willUpdateCountryCode = (myAccount.countryCode != command.countryCode)
                val willUpdateTimeZoneCode = (myAccount.timeZoneCode != command.timeZoneCode)
                if (willUpdateLanguageCode || willUpdateCountryCode || willUpdateTimeZoneCode) {
                    myAccount = myAccount.updateLocaleInfo(
                        languageCode = command.languageCode,
                        countryCode = command.countryCode,
                        timeZoneCode = command.timeZoneCode
                    )
                    // 기존 계정 데이터 업데이트
                    accountRepository.save(myAccount)
                }
            }

            return myAccount.toAccountForResponse()
        } catch (e: Throwable) {
            logger().error("command=$command", e)
            throw e
        }
    }
}
