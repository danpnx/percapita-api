package br.com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.project.service.EditProfileService;

@RestController
@RequestMapping("/edit-profileName")
public class EditProfileNameController {
	
	@Autowired
	EditProfileService editProfileService;
	
	@PutMapping
	public ResponseEntity<?> editName(@RequestParam Long username, String newName) {
		editProfileService.editName(username, newName);
		return ResponseEntity.status(HttpStatus.CREATED).body("Nome Alterado com sucesso!");
	}

}
