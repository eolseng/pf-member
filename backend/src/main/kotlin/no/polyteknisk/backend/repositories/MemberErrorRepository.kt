package no.polyteknisk.backend.repositories

import no.polyteknisk.backend.entities.MemberError
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberErrorRepository : JpaRepository<MemberError, Int>