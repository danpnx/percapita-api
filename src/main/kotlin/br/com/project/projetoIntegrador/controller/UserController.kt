package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.service.UserService
import br.com.project.projetoIntegrador.utils.ContextUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
class UserController @Autowired constructor(private val userService: UserService) {

    @GetMapping("/profile")
    fun getProfile(): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        return ResponseEntity.ok(userService.findByUsername(username))
    }
}