package no.polyteknisk.backend.repositories

import no.polyteknisk.backend.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String>