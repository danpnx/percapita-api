package br.com.project.projetoIntegrador.repositories

import br.com.project.projetoIntegrador.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun existsByUsername(username: String): Boolean
    fun findByUsername(username: String): User
    fun findByToken(token: String): Optional<User>
}