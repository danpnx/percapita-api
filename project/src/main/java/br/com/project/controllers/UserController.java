package br.com.project.controllers;

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

	@GetMapping("/profile")
	public ResponseEntity<User> getUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return ResponseEntity.ok(userService.findByUsername(username));
	}
}
