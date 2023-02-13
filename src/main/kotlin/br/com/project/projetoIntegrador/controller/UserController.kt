package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.models.User
import br.com.project.projetoIntegrador.payload.ProfilePayload
import br.com.project.projetoIntegrador.payload.StandardMessage
import br.com.project.projetoIntegrador.service.UserService
import br.com.project.projetoIntegrador.utils.ContextUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints para acesso ao usuário")
@SecurityRequirement(name = "Bearer Authentication")
class UserController @Autowired constructor(private val userService: UserService) {

    @Operation(
        summary = "Perfil do usuário",
        description = "Dados de perfil do usuário",
        tags = ["User"],
        responses = [
            ApiResponse(
                description = "OK",
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = User::class)
                    )
                ]
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("/profile")
    fun getProfile(): ResponseEntity<ProfilePayload> {
        val username: String = ContextUtils.getUsername()
        return ResponseEntity.ok(userService.findByUsername(username))
    }
}