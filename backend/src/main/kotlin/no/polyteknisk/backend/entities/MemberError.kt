package no.polyteknisk.backend.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class MemberError(
        @Id
        @GeneratedValue
        val id: Int = 0,
        val memberId: Int,
        val errorMessage: String
)