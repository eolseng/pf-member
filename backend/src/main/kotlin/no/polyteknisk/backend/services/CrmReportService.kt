package no.polyteknisk.backend.services

import no.polyteknisk.backend.entities.CrmRowDTO
import no.polyteknisk.backend.entities.Member
import org.apache.tika.Tika
import org.springframework.stereotype.Service
import java.io.InputStream
import java.time.LocalDate

@Service
class CrmReportService(
        private val errorService: ErrorService
) {

    fun parse(inputStream: InputStream): List<Member> {
        val xmlString = parseToString(inputStream)
        val dtos = convertToDTOs(xmlString)
        return convertToMembers(dtos)
    }

    private fun convertToMembers(dtos: List<CrmRowDTO>): List<Member> {
        // Filter out inactive rows
        val today = LocalDate.now()
        val activeRows = dtos.filter { it.endDate > today && it.startDate <= today }

        // Group rows by ID
        val rowsById = activeRows.groupBy { it.id }

        // Create Member objects
        val members = mutableListOf<Member>()
        rowsById.forEach { member ->
            checkForErrors(member)
            val memberObject = constructMember(member)
            memberObject?.let { members.add(it) }
        }
        return members
    }

    private fun parseToString(inputStream: InputStream): String {
        val parser = Tika()
        parser.maxStringLength = 10 * 1024 * 1024
        return parser.parseToString(inputStream)
    }

    private fun convertToDTOs(xmlString: String): List<CrmRowDTO> {

        // Creates a 2D array (Array<Array<String>>) of the XML-file.
        val xmlRows = xmlString.split('\n').map { line -> line.split('\t') }

        // Saves headers as a map (Header -> Index)
        val headerIndexes = xmlRows[0].mapIndexed { index, headerField -> headerField to index }.toMap()

        // Converts rows into profiles. Skips index 0 as this is the header row
        return xmlRows.subList(1, xmlRows.size)
                // Might contain empty rows ([]) - skip those to avoid NullPointerException
                .filter { it.size > 1 }
                // Map rows to Objects
                .map {
                    CrmRowDTO(
                            id = it[headerIndexes["Kundenr"] ?: error("0")].toInt(),
                            firstName = it[headerIndexes["Fornavn"] ?: error("")],
                            lastName = it[headerIndexes["Navn"] ?: error("")],
                            email = it[headerIndexes["E-post"] ?: error("")],
                            address = it[headerIndexes["Postadresse"] ?: error("")],
                            postalCode = it[headerIndexes["Post postnr."] ?: error("")],
                            areaCode = it[headerIndexes["Post poststed"] ?: error("")],
                            birthDate = it[headerIndexes["Org. nr."] ?: error("")],
                            type = it[headerIndexes["Type"] ?: error("")],
                            startDate = it[headerIndexes["Startdato"] ?: error("")].toLocalDate(),
                            endDate = it[headerIndexes["Sluttdato"] ?: error("")].toLocalDate(),
                            ordinaryMembership = it[headerIndexes["PM"] ?: error("0")].toBoolean(),
                            seniorMembership = it[headerIndexes["PM70+"] ?: error("0")].toBoolean(),
                            studentMembership = it[headerIndexes["Student"] ?: error("0")].toBoolean(),
                            teknaMembership = it[headerIndexes["Tekna"] ?: error("0")].toBoolean(),
                            businessMembership = it[headerIndexes["BM"] ?: error("0")].toBoolean(),
                            freeMembership = it[headerIndexes["Gratis"] ?: error("0")].toBoolean(),
                            customerType = it[headerIndexes["Kundetype"] ?: error("")]
                    )
                }
    }

    private fun checkForErrors(member: Map.Entry<Int, List<CrmRowDTO>>) {

        /*
            ERROR REPORTING:
            - A member must have 'Polyteknisk Forening' or 'PF Petroleum og fornybar'
            - A member should not have both 'Polyteknisk Forening' and 'PF Petroleum og fornybar'
            - A member should not have multiple "Membership Tiers" checked
            - A company should only have "Polyteknisk Forening" as type
            - A company should not have any "Membership Tiers" checked
        */

        // A member must have 'Polyteknisk Forening' or 'PF Petroleum og fornybar'
        if (!member.value.any { it.type == "Polyteknisk Forening" || it.type == "PF Petroleum og fornybar" }) {
            errorService.createMemberError(
                    member.key,
                    "Kunde har ikke 'Polyteknisk Forening' eller 'PF Petroleum og fornybar'"
            )
        }

        // A member should not have both 'Polyteknisk Forening' and 'PF Petroleum og fornybar'
        if (member.value.filter { it.type == "Polyteknisk Forening" || it.type == "PF Petroleum og fornybar" }.size > 1) {
            errorService.createMemberError(
                    member.key,
                    "Kunde har både 'Polyteknisk Forening' og 'PF Petroleum og fornybar'"
            )
        }

        // A member should not have multiple "Membership Tiers" checked
        member.value.forEach {
            if (it.ordinaryMembership && (it.seniorMembership || it.studentMembership) || (it.seniorMembership && it.studentMembership)) {
                errorService.createMemberError(
                        member.key,
                        "Kunde har flere av 'PM', 'PM70+' og 'Student' på rad med type ${it.type}"
                )
            }
        }

        // Checks for 'Company' rows
        member.value.filter { it.customerType == "Company" }.forEach {

            // A company should only have "Polyteknisk Forening" as type
            if (it.type != "Polyteknisk Forening") {
                errorService.createMemberError(
                        member.key,
                        "Bedrift skal kun ha rad med type 'Polyteknisk Forening' - fant rad med type ${it.type}"
                )
            }

            // A company should not have any "Membership Tiers" checked
            if (it.ordinaryMembership || it.seniorMembership || it.studentMembership) {
                errorService.createMemberError(
                        member.key,
                        "Bedrift skal ikke ha 'PM', 'PM70+' eller 'Student' - fant på rad med type ${it.type}"
                )
            }
        }
    }

    private fun constructMember(member: Map.Entry<Int, List<CrmRowDTO>>): Member? {
        val membershipType = getMembershipType(member)
        val membershipTier = getMembershipTier(member)
        val tekniskUkeblad = member.value.any { row -> row.type == "Teknisk Ukeblad" }
        return member.value
                .find { it.type == "Polyteknisk Forening" || it.type == "PF Petroleum og fornybar" }
                ?.let {
                    Member(
                            id = it.id,
                            lastName = it.lastName,
                            firstName = it.firstName,
                            email = it.email,
                            address = it.address,
                            postalCode = it.postalCode,
                            areaCode = it.areaCode,
                            birthDate = it.birthDate,
                            customerType = it.customerType,
                            membershipTier = membershipTier,
                            membershipType = membershipType,
                            freeMembership = it.freeMembership,
                            tekniskUkeblad = tekniskUkeblad
                    )
                }
    }

    private fun getMembershipTier(member: Map.Entry<Int, List<CrmRowDTO>>): Member.MembershipTier {

        val rows = member.value

        return when {
            rows.any { it.studentMembership } -> Member.MembershipTier.Student
            rows.any { it.seniorMembership } -> Member.MembershipTier.Senior
            rows.any { it.ordinaryMembership } -> Member.MembershipTier.Ordinary
            rows.any { it.customerType == "Company" } -> Member.MembershipTier.Bedrift
            else -> Member.MembershipTier.ERROR
        }
    }

    private fun getMembershipType(member: Map.Entry<Int, List<CrmRowDTO>>): Member.MembershipType {

        val rows = member.value

        return when {
            rows.any { it.customerType == "Company" } -> {
                Member.MembershipType.Bedrift
            }
            rows.any { it.businessMembership } -> {
                Member.MembershipType.Bedriftsmedlem
            }
            rows.any { it.teknaMembership } -> {
                Member.MembershipType.Tekna
            }
            rows.any { it.type == "PF Petroleum og fornybar" } -> {
                Member.MembershipType.PFPetroleumOgFornybar
            }
            rows.any { it.type == "Polyteknisk Forening" } -> {
                Member.MembershipType.PolytekniskForening
            }
            else -> Member.MembershipType.ERROR
        }
    }

    private fun String.toLocalDate(): LocalDate {
        val splitString = this.split("-")
        val year: Int = splitString[0].toInt()
        val month: Int = splitString[1].toInt()
        val day: Int = splitString[2].substring(0, 2).toInt()
        return LocalDate.of(year, month, day)
    }

    private fun String.toBoolean(): Boolean {
        return this == "1"
    }

}