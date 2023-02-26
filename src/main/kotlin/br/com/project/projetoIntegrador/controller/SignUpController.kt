package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.exceptions.DataNotAvailableException
import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.models.User
import br.com.project.projetoIntegrador.models.UserLogin
import br.com.project.projetoIntegrador.payload.StandardMessage
import br.com.project.projetoIntegrador.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin("*")
@RequestMapping("/signup")
@Tag(name = "Sign up", description = "Endpoint para criação de conta no aplicativo")
class SignUpController @Autowired private constructor(val userService: UserService) {

    @Operation(
        summary = "Criar conta",
        description = "Criação de conta no aplicativo",
        tags = ["Sign up"],
        responses = [
            ApiResponse(
                description = "CREATED",
                responseCode = "201",
                content = [
                    Content(schema = Schema(implementation = String::class))
                ]
            ),
            ApiResponse(
                description = "CONFLICT",
                responseCode = "409",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
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
    @PostMapping
    fun signUp(@RequestBody @Valid user: User): ResponseEntity<Any> {
//        val status: HttpStatus = userService.register(user)
//       if(status == HttpStatus.BAD_REQUEST) {
//            throw InvalidInputException("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial")
//        }
//        if(status == HttpStatus.CONFLICT) {
//            throw DataNotAvailableException("Email já está em uso");
//        }
//        return ResponseEntity.status(HttpStatus.CREATED).body("Conta criada com sucesso!")
        val newUser = userService.register(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser)
    }
}