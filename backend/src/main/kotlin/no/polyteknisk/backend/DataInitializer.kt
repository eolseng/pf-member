package no.polyteknisk.backend

import no.polyteknisk.backend.services.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataInitializer(
        private val userService: UserService
) : CommandLineRunner{

    override fun run(vararg args: String?) {
        createAdminUser()
    }

    private fun createAdminUser() {
        val username = getAdminUsernameOrDefault()
        val password = getDefaultAdminPasswordOrDefault()
        val roles = setOf("USER", "ADMIN")
        userService.createUser(username, password, roles)
    }

    private fun getDefaultAdminPasswordOrDefault(): String {
        val adminPasswordEnvVariable = "ADMIN_PASSWORD"
        val defaultAdminPassword = "admin"
        return (System.getenv(adminPasswordEnvVariable) ?: defaultAdminPassword).trim()
    }

    private fun getAdminUsernameOrDefault(): String {
        val adminUsernameEnvVariable = "ADMIN_USERNAME"
        val defaultAdminUsername = "admin"
        return (System.getenv(adminUsernameEnvVariable) ?: defaultAdminUsername).trim()
    }

}