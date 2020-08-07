package no.polyteknisk.backend.services

import no.polyteknisk.backend.entities.User
import no.polyteknisk.backend.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class UserService(
        private val repository: UserRepository,
        private val passwordEncoder: PasswordEncoder
) {

    fun createUser(
            username: String,
            password: String,
            roles: Set<String> = setOf()
    ): Boolean {
        try {
            val hash = passwordEncoder.encode(password)

            val caseInsensitiveUsername = username.toLowerCase()
            if (repository.existsById(caseInsensitiveUsername)) return false

            val user = User(
                    username = caseInsensitiveUsername,
                    password = hash,
                    roles = roles.map { "ROLE_$it" }.toSet()
            )
            repository.save(user)
            return false
        } catch (e: Exception) {
            return false
        }
    }

}