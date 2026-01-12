package com.example.springbootkotlinwebflux.infrastructure.persistence.jpa.member

import com.example.springbootkotlinwebflux.domain.member.Member
import com.example.springbootkotlinwebflux.domain.member.MemberRepository
import com.example.springbootkotlinwebflux.domain.member.ThirdPartyAuthType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl(
    private val springDataMemberRepository: SpringDataMemberRepository,
    private val ioDispatcher: CoroutineDispatcher
) : MemberRepository {

    override suspend fun save(member: Member, willDelete: Boolean): Member = withContext(ioDispatcher) {
        springDataMemberRepository.save(
            MemberEntity(member, willDelete)
        ).awaitSingle().toMember()
    }

    override suspend fun findTopByThirdPartyAuthTypeAndThirdPartyAuthUidAndDeletedAtIsNullOrderByIdDesc(
        thirdPartyAuthType: ThirdPartyAuthType,
        thirdPartyAuthUid: String
    ): Member? = withContext(ioDispatcher) {
        springDataMemberRepository.findTopByThirdPartyAuthTypeAndThirdPartyAuthUidAndDeletedAtIsNullOrderByIdDesc(
            thirdPartyAuthType,
            thirdPartyAuthUid
        ).awaitSingleOrNull()?.toMember()
    }

    override suspend fun findTopByIdAndDeletedAtIsNull(id: Long): Member? = withContext(ioDispatcher) {
        springDataMemberRepository.findTopByIdAndDeletedAtIsNull(id).awaitSingleOrNull()?.toMember()
    }

    override suspend fun updateMemberLocale(
        id: Long,
        languageCode: String,
        countryCode: String,
        timeZoneCode: String
    ): Unit = withContext(ioDispatcher) {
        springDataMemberRepository.updateMemberLocale(
            id = id,
            languageCode = languageCode,
            countryCode = countryCode,
            timeZoneCode = timeZoneCode
        ).awaitSingleOrNull()
    }
}
