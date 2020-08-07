package no.polyteknisk.backend.entities

import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name="USERS")
data class User(
        @Id
        val username: String,
        @NotBlank
        val password: String,

        @NotNull
        @ElementCollection
        val roles: Set<String> = setOf(),

        @NotNull
        val enabled: Boolean = true
)