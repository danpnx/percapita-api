package br.com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import br.com.project.models.User;
import br.com.project.service.UserService;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	// Endpoint para teste
	@GetMapping("/all")
	public ResponseEntity<List<User>> getAll() {
		return ResponseEntity.ok(userService.findAll());
	}

	// Endpoint para alterar a senha do usuário
	@PutMapping("/edit/password")
	public ResponseEntity<?> updatePassword(@RequestParam String actualPassword, @RequestParam String newPassword) {
		// Recebe o username do usuário autenticado
		String usernameContext = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

		userService.updatePassword(actualPassword, newPassword, usernameContext);
		return ResponseEntity.ok().build();
	}

	// Endpoint para alterar o nome do usuário
	@PutMapping("/edit/name")
	public ResponseEntity<?> updateName(@RequestParam String newName) {
		// Recebe o username do usuário autenticado
		String usernameContext = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

		userService.updateName(newName, usernameContext);
		return ResponseEntity.ok().build();
	}
}
