package br.com.project.controllers

import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.models.User
import br.com.project.projetoIntegrador.models.UserLogin
import br.com.project.projetoIntegrador.service.EditProfileService
import br.com.project.projetoIntegrador.utils.ContextUtils
import br.com.project.projetoIntegrador.utils.InputUtils
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
class EditProfileController @Autowired private constructor(val editProfileService: EditProfileService) {

    @PutMapping("/edit-password")
    fun editPassword(@RequestBody newPassword: UserLogin): ResponseEntity<Any> {
       val username: String = ContextUtils.getUsername()
        if(!InputUtils.validatePassword(newPassword.toString())) {
            throw InvalidInputException ("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial")
        }
            editProfileService.editPassword(username, newPassword.password.toString())
        return status(HttpStatus.CREATED).body("Senha alterada com sucesso!")
    }

    @PutMapping("/edit-name")
    fun editName(@RequestParam newName: String): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        editProfileService.editName(username, newName)
        return status(HttpStatus.CREATED).body("Nome alterado com sucesso!")
    }
}