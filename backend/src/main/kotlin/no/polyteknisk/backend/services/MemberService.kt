package no.polyteknisk.backend.services

import no.polyteknisk.backend.entities.Member
import no.polyteknisk.backend.entities.Member.MembershipTier
import no.polyteknisk.backend.entities.Member.MembershipType
import no.polyteknisk.backend.repositories.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
        val repository: MemberRepository,
        val errorService: MemberErrorService
) {

    fun findAll(): List<Member> {
        return repository.findAll()
    }

    fun saveAll(members: List<Member>) {
        members.forEach { validate(it) }
        repository.saveAll(members)
    }

    fun deleteAllMembers() {
        repository.deleteAllMembers()
    }

    private fun validate(member: Member) {
        when (member.membershipType) {
            MembershipType.Bedrift -> validateBedrift(member)
            MembershipType.Bedriftsmedlem -> validateBedriftsmedlem(member)
            MembershipType.Tekna -> validateTekna(member)
            MembershipType.PFPetroleumOgFornybar -> validatePFPetroleumOfFornybar(member)
            else -> {
            }
        }
    }

    private fun validateBedrift(member: Member) {
        if (member.freeMembership) {
            errorService.createError(
                    member.id,
                    "Bedrifter skal ikke ha 'Gratis'"
            )
        }
        if (member.tekniskUkeblad) {
            errorService.createError(
                    member.id,
                    "Bedrifter skal ikke ha 'Teknisk ukeblad'"
            )
        }
    }

    private fun validateBedriftsmedlem(member: Member) {
        if (!member.freeMembership) {
            errorService.createError(
                    member.id,
                    "Bedriftsmedlemmer skal ha 'Gratis'"
            )
        }
        if (!member.membershipTier.equals(MembershipTier.Ordinary)) {
            errorService.createError(
                    member.id,
                    "Bedriftsmedlemmer skal ha 'PM'"
            )
        }
    }

    private fun validateTekna(member: Member) {
        if (member.freeMembership) {
            errorService.createError(
                    member.id,
                    "Tekna-medlemmer skal ikke ha 'Gratis'"
            )
        }
    }

    private fun validatePFPetroleumOfFornybar(member: Member) {
        if (member.freeMembership) {
            errorService.createError(
                    member.id,
                    "PF Petroleum og fornybar-medlemmer skal ikke ha 'Gratis'"
            )
        }
    }

}