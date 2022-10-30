package br.com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.project.service.PasswordRecoveryService;

@RestController
@RequestMapping("/register")
public class PasswordRecoveryController {

	@Autowired
	private PasswordRecoveryService passwordRecoveryService;

	@PostMapping("/forgot-password")
	public String forgotPassword(@RequestParam String email) {
		String response = passwordRecoveryService.forgotPassword(email);
		if (!response.startsWith("Invalid")) {
			response = "http://localhost:8080/register/reset-password?token=" + response;
			passwordRecoveryService.sendEmail(email, response);
			return "E-mail enviado com sucesso!";
		}
		return "Não foi possivel enviar o email";
	}

	@PutMapping("/reset-password")
	public String resetPassword(@RequestParam String token, @RequestParam String password) {
		if (!passwordRecoveryService.validatePassword(password)) {
			return "Sua senha deve conter no minimo 8 caracteres a 20, com pelo menos 1 letra(s) maiúscula e ao menos 1 caractere(s) especial.";
		}
		return passwordRecoveryService.resetPassword(token, password);
	}

}