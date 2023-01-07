package br.com.project.projetoIntegrador

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class ProjetoIntegradorApplication

fun main(args: Array<String>) {
	runApplication<ProjetoIntegradorApplication>(*args)
}
