package br.com.project.controllers;

import br.com.project.exceptions.InvalidInputException;
import br.com.project.models.StandardMessage;
import br.com.project.service.PasswordRecoveryService;
import br.com.project.utils.InputUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Password Recovery", description = "Endpoints para recuperar a senha do usuário")
public class PasswordRecoveryController {

    @Autowired
    private PasswordRecoveryService passwordRecoveryService;

    @Operation(
            summary = "Esqueci a senha",
            description = "Endpoint para quando o usuário esquecer a sua senha",
            tags = {"Password Recovery"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(
                            schema = @Schema(implementation = String.class)
                    )),
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    ))
            }
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        String token = passwordRecoveryService.forgotPassword(email);
        String response = "http://localhost:8080/reset-password?token=" + token;
        passwordRecoveryService.sendEmail(email, response);
        return ResponseEntity.ok("Email enviado com sucesso!");
    }

    @Operation(
            summary = "Criar uma nova senha",
            description = "Endpoint para criar uma nova senha",
            tags = {"Password Recovery"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(
                            schema = @Schema(implementation = String.class)
                    )),
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "UNAUTHORIZED", responseCode = "201", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    ))
            }
    )
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String password) {
        if (!InputUtils.validatePassword(password)) {
            throw new InvalidInputException("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial");
        }
        return ResponseEntity.ok(passwordRecoveryService.resetPassword(token, password));
    }
}
