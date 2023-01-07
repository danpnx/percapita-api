package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.service.PasswordRecoveryService
import br.com.project.projetoIntegrador.utils.InputUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PasswordRecoveryController @Autowired constructor(private val passwordRecoveryService: PasswordRecoveryService) {

    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestParam email: String): ResponseEntity<Any> {
        val token: Unit = passwordRecoveryService.forgotPassword(email)
        val response: String = "https://percapita.azurewebsites.net/reset-password?token=$token"
        passwordRecoveryService.sendEmail(email, response)
        return ResponseEntity.ok("Email enviado com sucesso!")
    }

    @PutMapping("/reset-password")
    fun resetPassword(@RequestParam token: String, @RequestParam password: String): ResponseEntity<Any> {
        if(!InputUtils.validatePassword(password)) {
            throw InvalidInputException("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial")
        }
        return ResponseEntity.ok(passwordRecoveryService.resetPassword(token, password))
    }
}