package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.dto.EditPasswordDTO
import br.com.project.projetoIntegrador.payload.StandardMessage
import br.com.project.projetoIntegrador.service.EditProfileService
import br.com.project.projetoIntegrador.utils.ContextUtils
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
import org.springframework.web.bind.annotation.*

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
    fun editPassword(@RequestBody passwordData: EditPasswordDTO): ResponseEntity<String> {
        val username: String = ContextUtils.getUsername()
        editProfileService.editPassword(username, passwordData)
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
    fun editName(@RequestParam newName: String): ResponseEntity<String> {
        val username: String = ContextUtils.getUsername()
        editProfileService.editName(username, newName)
        return status(HttpStatus.CREATED).body("Nome alterado com sucesso!")
    }

    @PutMapping("/edit-user")
    fun editUser(@RequestBody editUser: EditPasswordDTO): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        val response = editProfileService.editUser(username, editUser)
        return status(HttpStatus.OK).body(response)
    }
}