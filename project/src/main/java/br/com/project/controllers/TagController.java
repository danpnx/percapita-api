package br.com.project.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.project.enums.TagCategory;
import br.com.project.service.TagService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@CrossOrigin("*")
@RequestMapping("/tag")
public class TagController {

	@Autowired
	private TagService tagService;

	@PostMapping("/create")
	public ResponseEntity<?> createTag(@RequestBody String tagName, Long userId, TagCategory tagCategory) {
		if (tagService.existsByTagNameAndUser(tagName, userId)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe uma tag com este nome.");
		}
		tagService.registerTag(tagName, userId, tagCategory);
		return ResponseEntity.status(HttpStatus.CREATED).body("Tag " + tagName + " criada!");
	}

	@PutMapping("/edit")
	public ResponseEntity<?> editTag(@RequestParam UUID tagId, String newName) {
		tagService.editTag(tagId, newName);
		return ResponseEntity.status(HttpStatus.CREATED).body("Nome da tag atualizada.");
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteTag(@RequestParam String tagId) {
		tagService.deleteTag(UUID.fromString(tagId));
		return ResponseEntity.noContent().build();
	}
}
