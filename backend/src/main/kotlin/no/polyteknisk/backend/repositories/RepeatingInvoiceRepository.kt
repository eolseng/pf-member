package no.polyteknisk.backend.repositories

import no.polyteknisk.backend.entities.RepeatingInvoice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RepeatingInvoiceRepository: JpaRepository<RepeatingInvoice, Int>