package no.polyteknisk.backend.repositories

import no.polyteknisk.backend.entities.RepeatingInvoiceError
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RepeatingInvoiceErrorRepository: JpaRepository<RepeatingInvoiceError, Int>