package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.models.UserLogin
import br.com.project.projetoIntegrador.payload.StandardMessage
import br.com.project.projetoIntegrador.service.EditProfileService
import br.com.project.projetoIntegrador.utils.ContextUtils
import br.com.project.projetoIntegrador.utils.InputUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints para alterar nome e senha do usuário")
@SecurityRequirement(name = "Bearer Authentication")
class EditProfileController @Autowired constructor(private val editProfileService: EditProfileService) {

    @Operation(summary = "Alterar senha",
        description = "Endpoint para alterar a senha do usuário",
        tags = ["User"],
        responses = [
            ApiResponse(
                description = "CREATED",
                responseCode = "201",
                content = [
                    Content(schema = Schema(implementation = String::class))
                ]
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = StandardMessage::class))
                ]
            )
        ]
    )
    @PutMapping("/edit-password")
    fun editPassword(@RequestBody newPassword: UserLogin): ResponseEntity<Any> {
       val username: String = ContextUtils.getUsername()
        if(!InputUtils.validatePassword(newPassword.toString())) {
            throw InvalidInputException ("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial")
        }
            editProfileService.editPassword(username, newPassword.password.toString())
        return status(HttpStatus.CREATED).body("Senha alterada com sucesso!")
    }

    @Operation(summary = "Alterar nome",
        description = "Endpoint para alterar o nome do usuário",
        tags = ["User"],
        responses = [
            ApiResponse(
                description = "CREATED",
                responseCode = "201",
                content = [
                    Content(schema = Schema(implementation = String::class))
                ]
            )
        ]
    )
    @PutMapping("/edit-name")
    fun editName(@RequestParam newName: String): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        editProfileService.editName(username, newName)
        return status(HttpStatus.CREATED).body("Nome alterado com sucesso!")
    }
}