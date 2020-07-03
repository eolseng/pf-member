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

    fun findAllByMembershipStatusAndTekniskUkeblad(status: Member.MembershipStatus, tekniskUkeblad: Boolean): List<Member>

    fun countAllByMembershipStatusAndMembershipTypeAndMembershipTier(status: Member.MembershipStatus, type: Member.MembershipType, tier: Member.MembershipTier): Int

    fun countAllByMembershipStatusAndMembershipTypeAndMembershipTierAndFreeMembership(status: Member.MembershipStatus, type: Member.MembershipType, tier: Member.MembershipTier, free: Boolean): Int

}