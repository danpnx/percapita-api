package br.com.project.controllers;

import br.com.project.exceptions.DataNotAvailableException;
import br.com.project.exceptions.InvalidInputException;
import br.com.project.models.User;
import br.com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> signup(@RequestBody @Valid User user) {
        HttpStatus status =  userService.register(user);

        if(status.equals(HttpStatus.BAD_REQUEST)) {
            throw new InvalidInputException("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial");
        }

        if(status.equals(HttpStatus.CONFLICT)) {
            throw new DataNotAvailableException("Email já está em uso");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Conta criada com sucesso!");
    }
}
