package no.polyteknisk.backend.entities

import org.springframework.data.rest.core.config.Projection
import java.time.LocalDate
import javax.persistence.*

@Entity(name = "Member")
@Table(name = "member")
class Member(
        @Id
        val id: Int,
        val lastName: String,
        val firstName: String,
        val email: String,
        val address: String,
        val postalCode: String,
        val areaCode: String,
        val birthDate: String,
        val customerType: String,
        val startDate: LocalDate,
        val endDate: LocalDate,
        val membershipStatus: MembershipStatus,
        val membershipType: MembershipType,
        val membershipTier: MembershipTier,
        val freeMembership: Boolean,
        val tekniskUkeblad: Boolean
) {

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "member",
            cascade = [(CascadeType.ALL)],
            orphanRemoval = true
    )
    val repeatingInvoices = mutableListOf<RepeatingInvoice>()

    enum class MembershipStatus {
        Deactivated,
        Active,
        Scheduled,
        ERROR
    }

    enum class MembershipType {
        PolytekniskForening,
        PFPetroleumOgFornybar,
        Tekna,
        Bedriftsmedlem,
        Bedrift,
        ERROR
    }

    enum class MembershipTier {
        Ordinary,
        Senior,
        Student,
        Bedrift,
        ERROR
    }
}

data class MemberStats(
        val types: List<MemberStatsType>
)

data class MemberStatsType(
        val type: Member.MembershipType,
        val tiers: List<MemberStatsTier>
)

data class MemberStatsTier(
        val type: Member.MembershipType,
        val tier: Member.MembershipTier,
        val paying: Int,
        val free: Int,
        val total: Int
)

@Projection(
        name = "excerpt",
        types = [Member::class]
)
interface MemberProjection {
    fun getId(): Int
    fun getLastName(): String
    fun getFirstName(): String
    fun getCustomerType(): String
    fun getMembershipType(): Member.MembershipType
    fun getMembershipTier(): Member.MembershipTier
    fun getFreeMembership(): Boolean
    fun getTekniskUkeblad(): Boolean
}