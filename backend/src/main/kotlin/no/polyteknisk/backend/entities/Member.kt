package no.polyteknisk.backend.entities

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
        val membershipType: MembershipType,
        val membershipTier: MembershipTier,
        val freeMembership: Boolean,
        val tekniskUkeblad: Boolean
) {

    @OneToMany(
            mappedBy = "member",
            cascade = [(CascadeType.ALL)],
            orphanRemoval = true
    )
    val repeatingInvoices: List<RepeatingInvoice>

    init {
        repeatingInvoices = mutableListOf()
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