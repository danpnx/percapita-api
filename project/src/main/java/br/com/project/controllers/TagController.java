package br.com.project.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class TagController {

	@Autowired
	private TagService tagService;

	@PostMapping("/create")
	public ResponseEntity<?> createTag(@RequestBody String tagName) {
		String username = getUsername();
		if (tagService.existsByTagNameAndUser(tagName, username)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe uma tag com este nome.");
		}
		tagService.registerTag(tagName, username);
		return ResponseEntity.status(HttpStatus.CREATED).body("Tag " + tagName + " criada!");
	}

	@PutMapping("/edit")
	public ResponseEntity<?> editTag(@RequestParam String tagId, String newName) {
		String username = getUsername();
		tagService.editTag(UUID.fromString(tagId), newName, username);
		return ResponseEntity.status(HttpStatus.CREATED).body("Nome da tag atualizada.");
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteTag(@RequestParam String tagId) {
		String username = getUsername();
		tagService.deleteTag(UUID.fromString(tagId), username);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Tag>> getAll() {
		String username = getUsername();
		return ResponseEntity.ok(tagService.getAllTags(username));
	}
	
	@GetMapping("/by-id")
	public ResponseEntity<Tag> getTagById(@RequestParam String tagId) {
        String username = getUsername();
        return ResponseEntity.ok(tagService.getTagById(UUID.fromString(tagId), username));
	}
	
	private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
