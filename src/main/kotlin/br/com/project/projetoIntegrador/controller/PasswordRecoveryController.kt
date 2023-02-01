package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.payload.StandardMessage
import br.com.project.projetoIntegrador.service.PasswordRecoveryService
import br.com.project.projetoIntegrador.utils.InputUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.models.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Password Recovery", description = "Endpoints para recuperar a senha do usuário")
class PasswordRecoveryController @Autowired constructor(private val passwordRecoveryService: PasswordRecoveryService) {

    @Operation(
        summary = "Esqueci a senha",
        description = "Endpoint para a recuperação de senha",
        tags = ["Password Recovery"],
        responses = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                description = "CREATED",
                responseCode = "201",
                content = [
                    Content(schema = Schema(implementation = String::class))
                ]
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = StandardMessage::class))
                ]
            ),
        ]
    )
    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestParam email: String): ResponseEntity<Any> {
        val token: Unit = passwordRecoveryService.forgotPassword(email)
        val response: String = "http://localhost:8080/reset-password?token=$token"
        passwordRecoveryService.sendEmail(email, response)
        return ResponseEntity.ok("Email enviado com sucesso!")
    }

    @Operation(
        summary = "Criar uma nova senha",
        description = "Endpoint para a criação de uma nova senha",
        tags = ["Password Recovery"],
        responses = [
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                description = "OK",
                responseCode = "200",
                content = [
                    Content(schema = Schema(implementation = String::class))
                ]
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = StandardMessage::class))
                ]
            ),
            io.swagger.v3.oas.annotations.responses.ApiResponse(
                description = "UNAUTHORIZED",
                responseCode = "401",
                content = [
                    Content(schema = Schema(implementation = StandardMessage::class))
                ]
            )
        ]
    )
    @PutMapping("/reset-password")
    fun resetPassword(@RequestParam token: String, @RequestParam password: String): ResponseEntity<Any> {
        if(!InputUtils.validatePassword(password)) {
            throw InvalidInputException("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial")
        }
        return ResponseEntity.ok(passwordRecoveryService.resetPassword(token, password))
    }
}