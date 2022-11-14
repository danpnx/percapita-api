package br.com.project.controllers;

import br.com.project.exceptions.InvalidInputException;
import br.com.project.service.PasswordRecoveryService;
import br.com.project.utils.InputUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PasswordRecoveryController {

    @Autowired
    private PasswordRecoveryService passwordRecoveryService;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        String token = passwordRecoveryService.forgotPassword(email);

        if (!token.equals("Email inválido!")) {
            String response = "http://localhost:8080/reset-password?token=" + token;
            passwordRecoveryService.sendEmail(email, response);
            return "Email enviado com sucesso!";
        }

        return "Não foi possivel enviar o email";
    }

    @PutMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password) {
        if (!InputUtils.validatePassword(password)) {
            throw new InvalidInputException("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial");
        }
        return passwordRecoveryService.resetPassword(token, password);
    }
}
