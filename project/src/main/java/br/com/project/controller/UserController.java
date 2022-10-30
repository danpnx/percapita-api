package br.com.project.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.project.models.User;
import br.com.project.service.UserService;

@RestController
@CrossOrigin("*")
@RequestMapping("/register")
public class UserController {

	@Autowired
	private UserService userService;

	// somente para teste esse get irá sair
	@GetMapping
	public ResponseEntity<List<User>> getAll() {
		return ResponseEntity.ok(userService.findAll());
	}

	@PostMapping("/signup")
	public ResponseEntity<Object> save(@RequestBody @Valid User userRegister) {
		if (!userService.validatePassword(userRegister.getPassword())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					"Sua senha deve conter no minimo 8 caracteres a 20, com pelo menos 1 letra(s) maiúscula e ao menos 1 caractere(s) especial.");
		}
		if (userService.existsByUsername(userRegister.getUsername())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já está em uso.");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userRegister));
	}
}
