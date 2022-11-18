package br.com.project.controllers;

import br.com.project.exceptions.InvalidInputException;
import br.com.project.payload.StandardMessage;
import br.com.project.utils.ContextUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.project.service.EditProfileService;
import br.com.project.utils.InputUtils;

@RestController
@RequestMapping("/user")
@Tag(name = "Editar perfil", description = "Controller para alterar nome e senha do usuário")
@SecurityRequirement(name = "Bearer Authentication")
public class EditProfileController {

	@Autowired
	EditProfileService editProfileService;

	@Operation(summary = "Alterar senha",
			description = "Endpoint para alterar a senha do usuário",
			tags = {"Editar perfil"},
			responses = {
					@ApiResponse(description = "CREATED", responseCode = "201", content = @Content(
							schema = @Schema(implementation = String.class)
					)),
					@ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = StandardMessage.class)
					))
			}
	)
	@PutMapping("/edit-password")
	public ResponseEntity<?> editPassword(String newPassword) {
		String username = ContextUtils.getUsername();
		if (!InputUtils.validatePassword(newPassword)) {
			throw new InvalidInputException("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial");
		}
		editProfileService.editPassword(username, newPassword);
		return ResponseEntity.status(HttpStatus.CREATED).body("Senha Alterada com sucesso!");
	}

	@Operation(
			summary = "Alterar nome",
			description = "Endpoint para alterar o nome do usuário",
			tags = {"Editar perfil"},
			responses = {
					@ApiResponse(description = "CREATED", responseCode = "201", content = @Content(
							schema = @Schema(implementation = String.class)
					))
			}
	)
	@PutMapping("/edit-name")
	public ResponseEntity<?> editName(String newName) {
		String username = ContextUtils.getUsername();
		editProfileService.editName(username, newName);
		return ResponseEntity.status(HttpStatus.CREATED).body("Nome Alterado com sucesso!");
	}
}
