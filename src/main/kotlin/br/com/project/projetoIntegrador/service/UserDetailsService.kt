package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.repositories.UserRepository
import br.com.project.projetoIntegrador.security.UserSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
        return UserSecurity(
            user.get().id,
            user.get().username,
            user.get().password,
            Collections.singleton(SimpleGrantedAuthority("user"))
        )
    }
}