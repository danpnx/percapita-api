package br.com.project.projetoIntegrador.security

object SecurityConstants {
    const val EXPIRATION: Long = 18_000_000
    const val SECRET: String = "cHJvamV0byBpbnRlZ3JhZG9yIGRpZ2l0YWwgaG91c2UgZSBlbXBpcmljdXM="
    const val TOKEN_PREFIX: String = "Bearer "
    const val HEADER: String = "Authorization"
}