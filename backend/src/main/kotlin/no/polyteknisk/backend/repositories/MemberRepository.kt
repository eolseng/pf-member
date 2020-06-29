package no.polyteknisk.backend.repositories

import no.polyteknisk.backend.entities.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : JpaRepository<Member, Int> {

    @Modifying
    @Query("DELETE FROM Member")
    fun deleteAllMembers()

    fun findAllByTekniskUkeblad(boolean: Boolean) : List<Member>

}