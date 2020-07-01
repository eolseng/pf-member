package no.polyteknisk.backend.services

import no.polyteknisk.backend.entities.MemberError
import no.polyteknisk.backend.entities.RepeatingInvoiceError
import no.polyteknisk.backend.repositories.MemberErrorRepository
import no.polyteknisk.backend.repositories.RepeatingInvoiceErrorRepository
import org.springframework.stereotype.Service

@Service
class ErrorService(
        val memberErrorRepository: MemberErrorRepository,
        val repeatingInvoiceErrorRepository: RepeatingInvoiceErrorRepository
) {
    fun createMemberError(memberId: Int, errorMsg: String) {
        val error = MemberError(memberId = memberId, errorMessage = errorMsg)
        memberErrorRepository.save(error)
    }

    fun findAllMemberErrors(): List<MemberError> {
        return memberErrorRepository.findAll()
    }

    fun createRepeatingInvoiceError(memberId: Int, invoiceId: String?, errorMsg: String) {
        val error = RepeatingInvoiceError(
                memberId = memberId,
                invoiceId = invoiceId,
                errorMsg = errorMsg
        )
        repeatingInvoiceErrorRepository.save(error)
    }

    fun findAllRepeatingInvoiceErrors(): List<RepeatingInvoiceError> {
        return repeatingInvoiceErrorRepository.findAll()
    }

}