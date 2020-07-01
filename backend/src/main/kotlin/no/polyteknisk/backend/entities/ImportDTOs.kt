package no.polyteknisk.backend.entities

import java.time.LocalDate

data class CrmRowDTO(
        val id: Int,
        val lastName: String,
        val firstName: String,
        val email: String,
        val address: String,
        val postalCode: String,
        val areaCode: String,
        val birthDate: String,
        val type: String,
        val startDate: LocalDate,
        val endDate: LocalDate,
        val ordinaryMembership: Boolean,
        val seniorMembership: Boolean,
        val studentMembership: Boolean,
        val teknaMembership: Boolean,
        val businessMembership: Boolean,
        val freeMembership: Boolean,
        val customerType: String
)

data class RepeatingInvoiceDTO(
        val invoiceId: String,
        val customerId: Int,
        val productId: Int,
        val productName: String,
        val price: Int,
        val dynamicPrice: Boolean,
        val frequency: String,
        val nextDate: LocalDate
)