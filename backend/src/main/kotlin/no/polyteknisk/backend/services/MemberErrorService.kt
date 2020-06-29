package no.polyteknisk.backend.services

import no.polyteknisk.backend.entities.MemberError
import no.polyteknisk.backend.repositories.MemberErrorRepository
import org.springframework.stereotype.Service

@Service
class MemberErrorService (
        val repository: MemberErrorRepository
){
    fun createError(memberId: Int, errorMsg: String) {
        val error = MemberError(memberId = memberId, errorMessage = errorMsg)
        repository.save(error)
    }

}