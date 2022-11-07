package br.com.project.controllers;

import br.com.project.exceptions.BadCredentialsException;
import br.com.project.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.project.service.EditProfileService;
import br.com.project.utils.Utilities;

@RestController
@RequestMapping("/user")
public class EditProfileController {

	@Autowired
	EditProfileService editProfileService;

	@PutMapping("/edit-profilePassword")
	public ResponseEntity<?> editPassword(@RequestParam Long username, String newPassword) {
		if (!Utilities.validatePassword(newPassword)) {
			throw new InvalidInputException("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial");
		}
		editProfileService.editPassword(username, newPassword);
		return ResponseEntity.status(HttpStatus.CREATED).body("Senha Alterada com sucesso!");
	}

	@PutMapping("/edit-profileName")
	public ResponseEntity<?> editName(@RequestParam Long username, String newName) {
		editProfileService.editName(username, newName);
		return ResponseEntity.status(HttpStatus.CREATED).body("Nome Alterado com sucesso!");
	}
}
