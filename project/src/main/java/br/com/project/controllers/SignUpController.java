package br.com.project.controllers;

import br.com.project.exceptions.DataNotAvailableException;
import br.com.project.exceptions.InvalidInputException;
import br.com.project.models.StandardMessage;
import br.com.project.models.User;
import br.com.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Sign up", description = "Controller para criação de conta no aplicativo")
public class SignUpController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Criar conta",
            description = "Endpoint para criar a conta no aplicativo",
            tags = {"Sign up"},
            responses = {
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "CONFLICT", responseCode = "409", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "CREATED", responseCode = "201", content = @Content(
                            schema = @Schema(implementation = String.class)
                    ))
            }
    )
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
