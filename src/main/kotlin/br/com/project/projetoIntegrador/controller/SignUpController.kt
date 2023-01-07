package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.exceptions.DataNotAvailableException
import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.models.User
import br.com.project.projetoIntegrador.models.UserLogin
import br.com.project.projetoIntegrador.service.UserService
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
class SignUpController @Autowired private constructor(val userService: UserService) {

    @PostMapping
    fun signUp(@RequestBody @Valid body: UserLogin): ResponseEntity<HttpStatus> {
        lateinit var user: User
        user.name = body.name
        user.username = body.username
        user.password = body.password
        return ResponseEntity.ok(userService.register(user))
//        if(status == HttpStatus.BAD_REQUEST) {
//            throw InvalidInputException("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial")
//        }
//        if(status == HttpStatus.CONFLICT) {
//            throw DataNotAvailableException("Email já está em uso");
//        }
//        return ResponseEntity.status(HttpStatus.CREATED).body("Conta criada com sucesso!")
    }
}