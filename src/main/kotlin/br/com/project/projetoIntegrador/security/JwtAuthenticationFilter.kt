package br.com.project.projetoIntegrador.security

import br.com.project.projetoIntegrador.models.UserLogin
import br.com.project.projetoIntegrador.security.SecurityConstants.EXPIRATION
import br.com.project.projetoIntegrador.security.SecurityConstants.SECRET
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*

class JwtAuthenticationFilter(
    @Autowired val authManager: AuthenticationManager
    ): UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): Authentication {
        val credentials = ObjectMapper().readValue(request?.inputStream, UserLogin::class.java)

        val authResult = UsernamePasswordAuthenticationToken(
            credentials.username, credentials.password)

        return authManager.authenticate(authResult)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val username = authResult?.principal.toString()

        val jwtToken = JWT.create()
            .withSubject(username)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION))
            .sign(Algorithm.HMAC512(SECRET.encodeToByteArray()))

        val headerBody = "$username $jwtToken"

        response?.setHeader("Authorization", headerBody)
        response?.addHeader("Access-Control-Expose-Headers", "Authorization")
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        failed: AuthenticationException?
    ) {
        throw BadCredentialsException("Email e/ou senha inválidos!")
    }
}