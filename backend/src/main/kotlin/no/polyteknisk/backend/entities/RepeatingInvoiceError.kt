package no.polyteknisk.backend.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class RepeatingInvoiceError(
        val memberId: Int? = null,
        val invoiceId: String? = null,
        val errorMsg: String? = null
) {

    @Id
    @GeneratedValue
    val id: Int = 0



}