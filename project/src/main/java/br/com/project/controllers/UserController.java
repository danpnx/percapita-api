package br.com.project.controllers;

import br.com.project.payload.ReportResponse;
import br.com.project.utils.ContextUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.project.models.User;
import br.com.project.service.UserService;

@RestController
@CrossOrigin("*")
@RequestMapping("/user")
@Tag(name = "Informações do usuário", description = "Controller para retornar dados do usuário")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
	@Autowired
	private UserService userService;

	// Endpoint para teste
//	@GetMapping("/all")
//	public ResponseEntity<List<User>> getAll() {
//		return ResponseEntity.ok(userService.findAll());
//	}

	@Operation(
			summary = "Perfil do usuário",
			description = "Endpoint para retornar dados do usuário",
			tags = {"Informações do usuário"},
			responses = {
					@ApiResponse(description = "OK", responseCode = "200", content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = User.class)
					)),
					@ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ReportResponse.class)
					))
			}
	)
	@GetMapping("/profile")
	public ResponseEntity<User> getUser() {
		String username = ContextUtils.getUsername();
		return ResponseEntity.ok(userService.findByUsername(username));
	}
}
