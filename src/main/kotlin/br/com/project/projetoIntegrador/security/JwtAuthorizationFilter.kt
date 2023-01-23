package br.com.project.projetoIntegrador.security

import br.com.project.projetoIntegrador.security.SecurityConstants.HEADER
import br.com.project.projetoIntegrador.security.SecurityConstants.SECRET
import br.com.project.projetoIntegrador.security.SecurityConstants.TOKEN_PREFIX
import br.com.project.projetoIntegrador.service.UserDetailsService
import br.com.project.projetoIntegrador.service.UserService
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.tomcat.util.http.parser.Authorization
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.util.Collections

class JwtAuthorizationFilter(authManager: AuthenticationManager,
    private val service: UserDetailsService
): BasicAuthenticationFilter(authManager) {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val token = request.getHeader(HEADER)

        if(token == null || !token.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response)
            return
        }

        val authToken = getAuthentication(token)

        SecurityContextHolder.getContext().authentication = authToken
        chain.doFilter(request, response)
    }

    private fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        val email = JWT.require(Algorithm.HMAC512(SECRET.encodeToByteArray()))
            .build()
            .verify(token.replace(TOKEN_PREFIX, ""))
            .subject
        val user = service.loadUserByUsername(email)

        return UsernamePasswordAuthenticationToken(email, null, user.authorities)
    }
}