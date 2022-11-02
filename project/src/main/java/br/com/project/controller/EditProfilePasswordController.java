package br.com.project.controller;

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
@RequestMapping("/edit-profilePassword")
public class EditProfilePasswordController {
	
	@Autowired
	EditProfileService editProfileService;
	
	@PutMapping
	public ResponseEntity<?> editPassword(@RequestParam Long username, String newPassword) {
		if (!Utilities.validatePassword(newPassword)) {
            return ResponseEntity.badRequest().body("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial.");
        }
		editProfileService.editPassword(username, newPassword);
		return ResponseEntity.status(HttpStatus.CREATED).body("Senha Alterada com sucesso!");
	}


}
