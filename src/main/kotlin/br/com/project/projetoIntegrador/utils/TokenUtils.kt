package br.com.project.projetoIntegrador.utils

import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID

class TokenUtils {

    companion object {
        private const val EXPIRE_TOKEN_AFTER_MINUTES = 30

        fun generateToken(): String {
            val token: StringBuilder = StringBuilder()
            return token.append(UUID.randomUUID()).append(UUID.randomUUID()).toString()
        }

        fun isTokenExpired(tokenCreationDate: LocalDateTime): Boolean {
            val now: LocalDateTime = LocalDateTime.now()
            val diff: Duration = Duration.between(tokenCreationDate, now)
            return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES
        }
    }
}