package br.com.project.controller;

import br.com.project.service.PasswordRecoveryService;
import br.com.project.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
public class PasswordRecoveryController {

    @Autowired
    private PasswordRecoveryService passwordRecoveryService;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        System.out.println("AT FORGOT PASSWORD - EMAIL: " + email);
        String token = passwordRecoveryService.forgotPassword(email);

        if (!token.equals("Email inválido!")) {
            String response = "http://localhost:8080/password/reset-password?token=" + token;
            passwordRecoveryService.sendEmail(email, response);
            return "Email enviado com sucesso!";
        }

        return "Não foi possivel enviar o email";
    }

    @PutMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password) {
        System.out.println("AT RESET PASSWORD - NEW PASSWORD: " + password);
        if (!Utilities.validatePassword(password)) {
            return "A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial.";
        }
        return passwordRecoveryService.resetPassword(token, password);
    }
}
