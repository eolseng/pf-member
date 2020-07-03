package no.polyteknisk.backend.entities

import java.time.LocalDate
import javax.persistence.*

@Entity(name = "RepeatingInvoice")
@Table(name = "repeating_invoice")
class RepeatingInvoice (
        @Id
        @GeneratedValue
        val id: Int = 0,
        val invoiceId: String,
        @ManyToOne(fetch = FetchType.LAZY)
        val member: Member,
        val productId: Int,
        val productName: String,
        val price: Int,
        val dynamicPrice: Boolean,
        val frequency: String,
        val nextDate: LocalDate
)