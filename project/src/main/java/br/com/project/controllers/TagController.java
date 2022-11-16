package br.com.project.controllers;

import java.util.List;
import java.util.UUID;

import br.com.project.models.StandardMessage;
import br.com.project.utils.ContextUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.project.models.Tag;
import br.com.project.service.TagService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@CrossOrigin("*")
@RequestMapping("/tag")
@SecurityRequirement(name = "Bearer Authentication")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "Controller para requisições de tag do usuário")
public class TagController {

	@Autowired
	private TagService tagService;

	@Operation(
			summary = "Criar tag",
			description = "Endpoint para criação de uma tag",
			tags = {"Tag"},
			responses = {
					@ApiResponse(description = "CONFLICT", responseCode = "409", content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = String.class)
					)),
					@ApiResponse(description = "CREATED", responseCode = "201", content = @Content(
							schema = @Schema(implementation = String.class)
					))
			}
	)
	@PostMapping("/create")
	public ResponseEntity<?> createTag(@RequestBody String tagName) {
		String username = ContextUtils.getUsername();
		if (tagService.existsByTagNameAndUser(tagName, username)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe uma tag com este nome.");
		}
		tagService.registerTag(tagName, username);
		return ResponseEntity.status(HttpStatus.CREATED).body("Tag " + tagName + " criada!");
	}

	@Operation(
			summary = "Editar tag",
			description = "Endpoint para editar nome de uma tag",
			tags = {"Tag"},
			responses = {
					@ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = StandardMessage.class)
					)),
					@ApiResponse(description = "CONFLICT", responseCode = "409", content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = StandardMessage.class)
					)),
					@ApiResponse(description = "CREATED", responseCode = "201", content = @Content(
							schema = @Schema(implementation = String.class)
					))
			}
	)
	@PutMapping("/edit")
	public ResponseEntity<?> editTag(@RequestParam String tagId, String newName) {
		String username = ContextUtils.getUsername();
		tagService.editTag(UUID.fromString(tagId), newName, username);
		return ResponseEntity.status(HttpStatus.CREATED).body("Nome da tag atualizada.");
	}

	@Operation(
			summary = "Deletar tag",
			description = "Endpoint para deletar uma tag",
			tags = {"Tag"},
			responses = {
					@ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = StandardMessage.class)
					)),
					@ApiResponse(description = "NO CONTENT", responseCode = "204", content = @Content),
					@ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = StandardMessage.class)
					))
			}
	)
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteTag(@RequestParam String tagId) {
		String username = ContextUtils.getUsername();
		tagService.deleteTag(UUID.fromString(tagId), username);
		return ResponseEntity.noContent().build();
	}

	@Operation(
			summary = "Tags do usuário",
			description = "Endpoint para buscar todas as tags do usuário",
			tags = {"Tag"},
			responses = {
					@ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = StandardMessage.class)
					)),
					@ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = StandardMessage.class)
					)),
					@ApiResponse(description = "OK", responseCode = "200", content = @Content(
							mediaType = "application/json",
							array = @ArraySchema(schema = @Schema(implementation = Tag.class))
					))
			}
	)
	@GetMapping("/all")
	public ResponseEntity<List<Tag>> getAll() {
		String username = ContextUtils.getUsername();
		return ResponseEntity.ok(tagService.getAllTags(username));
	}

	@Operation(
			summary = "Tag do usuário",
			description = "Endpoint para buscar uma tag específica do usuário",
			tags = {"Tag"},
			responses = {
					@ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = StandardMessage.class)
					)),
					@ApiResponse(description = "FORBIDDEN", responseCode = "403", content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = StandardMessage.class)
					)),
					@ApiResponse(description = "OK", responseCode = "200", content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = Tag.class)
					))
			}
	)
	@GetMapping("/by-id")
	public ResponseEntity<Tag> getTagById(@RequestParam String tagId) {
        String username = ContextUtils.getUsername();
        return ResponseEntity.ok(tagService.getTagById(UUID.fromString(tagId), username));
	}
}
