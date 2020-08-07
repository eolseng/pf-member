package no.polyteknisk.backend.services

import no.polyteknisk.backend.entities.Member
import no.polyteknisk.backend.entities.Member.MembershipTier
import no.polyteknisk.backend.entities.Member.MembershipType
import no.polyteknisk.backend.entities.MemberStats
import no.polyteknisk.backend.entities.MemberStatsTier
import no.polyteknisk.backend.entities.MemberStatsType
import no.polyteknisk.backend.repositories.MemberRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class MemberService(
        val repository: MemberRepository,
        val errorService: ErrorService
) {

    fun findAll(): List<Member> {
        return repository.findAll()
    }

    fun saveAll(members: List<Member>) {
        members.forEach { validateCrm(it) }
        repository.saveAll(members)
    }

    fun deleteAllMembers() {
        repository.deleteAllMembers()
    }

    @Transactional
    fun getStats(): MemberStats {

        val types = MembershipType.values().map { type ->
            val tiers = MembershipTier.values().map { tier ->
                val paying = repository.countAllByMembershipStatusAndMembershipTypeAndMembershipTierAndFreeMembership(
                        status = Member.MembershipStatus.Active,
                        type = type,
                        tier = tier,
                        free = false
                )
                val free = repository.countAllByMembershipStatusAndMembershipTypeAndMembershipTierAndFreeMembership(
                        status = Member.MembershipStatus.Active,
                        type = type,
                        tier = tier,
                        free = true
                )
                val total = repository.countAllByMembershipStatusAndMembershipTypeAndMembershipTier(
                        status = Member.MembershipStatus.Active,
                        type = type,
                        tier = tier
                )
                MemberStatsTier(
                        type = type,
                        tier = tier,
                        paying = paying,
                        free = free,
                        total = total
                )
            }
            MemberStatsType(
                    type = type,
                    tiers = tiers
            )
        }

        return MemberStats(
                types = types
        )
    }

    @Transactional
    fun validateInvoices() {

        val tekniskUkebladProductId = 20
        val memberToProductMap = mapOf(
                Pair(MembershipType.PolytekniskForening, mapOf(
                        Pair(MembershipTier.Ordinary, 3),
                        Pair(MembershipTier.Senior, 5),
                        Pair(MembershipTier.Student, 12)
                )),
                Pair(MembershipType.PFPetroleumOgFornybar, mapOf(
                        Pair(MembershipTier.Ordinary, 41),
                        Pair(MembershipTier.Senior, 42),
                        Pair(MembershipTier.Student, 43)
                ))
        )

        // Sjekker at kunde har repeterende fakturaer i henhold til tilpassede skjemaer
        val members = repository.findAll()
                .filter { !it.freeMembership && it.membershipStatus == Member.MembershipStatus.Active }
        for (member in members) {

            // List of all repeating invoices the member currently has
            val has = member.repeatingInvoices.map { it.productId }

            // List of all repeating invoices the member should have
            val shouldHave = mutableListOf<Int>()
            if (member.membershipType == MembershipType.PolytekniskForening || member.membershipType == MembershipType.PFPetroleumOgFornybar) {
                shouldHave.add(memberToProductMap[member.membershipType]!![member.membershipTier]!!)
            }
            if (member.tekniskUkeblad) {
                shouldHave.add(tekniskUkebladProductId)
            }

            // Check if the member has any repeating invoices that he should not have. Creates errors for the ones he should not have
            val shouldNotHave = has.subtract(shouldHave)
            for (id in shouldNotHave) {
                val invoice = member.repeatingInvoices.first { it.productId == id }
                errorService.createRepeatingInvoiceError(
                        memberId = member.id,
                        invoiceId = null,
                        errorMsg = "Kunde skal ikke ha repeterende faktura for '${invoice.productName}' - Produkt ID: $id"
                )
            }

            // Check if the member has all the repeating invoices he should have. Creates errors for the ones missing
            val missing = shouldHave.subtract(has)
            for (id in missing) {
                if (id == tekniskUkebladProductId) {
                    errorService.createRepeatingInvoiceError(
                            memberId = member.id,
                            invoiceId = null,
                            errorMsg = "Kunde mangler repeterende faktura for 'Teknisk Ukeblad' - Produkt ID: $id"
                    )
                } else {
                    errorService.createRepeatingInvoiceError(
                            memberId = member.id,
                            invoiceId = null,
                            errorMsg = "Kunde mangler repeterende faktura for '${member.membershipType} - ${member.membershipTier}' - Produkt ID: $id"
                    )
                }
            }
        }
    }

    private fun validateCrm(member: Member) {

        if (member.membershipStatus != Member.MembershipStatus.Active) return

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
            errorService.createMemberError(
                    member.id,
                    "Bedrifter skal ikke ha 'Gratis'"
            )
        }
        if (member.tekniskUkeblad) {
            errorService.createMemberError(
                    member.id,
                    "Bedrifter skal ikke ha 'Teknisk ukeblad'"
            )
        }
    }

    private fun validateBedriftsmedlem(member: Member) {
        if (!member.freeMembership) {
            errorService.createMemberError(
                    member.id,
                    "Bedriftsmedlemmer skal ha 'Gratis'"
            )
        }
        if (!member.membershipTier.equals(MembershipTier.Ordinary)) {
            errorService.createMemberError(
                    member.id,
                    "Bedriftsmedlemmer skal ha 'PM'"
            )
        }
    }

    private fun validateTekna(member: Member) {
        if (member.freeMembership) {
            errorService.createMemberError(
                    member.id,
                    "Tekna-medlemmer skal ikke ha 'Gratis'"
            )
        }
    }

    private fun validatePFPetroleumOfFornybar(member: Member) {
        if (member.freeMembership) {
            errorService.createMemberError(
                    member.id,
                    "PF Petroleum og fornybar-medlemmer skal ikke ha 'Gratis'"
            )
        }
    }

}