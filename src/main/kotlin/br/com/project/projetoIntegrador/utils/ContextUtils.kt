package br.com.project.projetoIntegrador.utils

import org.springframework.security.core.context.SecurityContextHolder

class ContextUtils {

    companion object {
        fun getUsername(): String {
            return SecurityContextHolder.getContext().authentication.principal.toString()
        }
    }
}