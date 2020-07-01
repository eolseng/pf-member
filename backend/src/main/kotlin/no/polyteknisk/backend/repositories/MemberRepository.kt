package no.polyteknisk.backend.repositories

import no.polyteknisk.backend.entities.Member
import no.polyteknisk.backend.entities.MemberProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import javax.transaction.Transactional

@RepositoryRestResource(excerptProjection = MemberProjection::class)
interface MemberRepository : JpaRepository<Member, Int> {

    @Modifying
    @Query("DELETE FROM Member")
    @Transactional
    fun deleteAllMembers()

    fun findAllByTekniskUkeblad(boolean: Boolean): List<Member>

    fun countAllByMembershipTypeAndMembershipTier(type: Member.MembershipType, tier: Member.MembershipTier): Int

    fun countAllByMembershipTypeAndMembershipTierAndFreeMembership(type: Member.MembershipType, tier: Member.MembershipTier, free: Boolean): Int

}