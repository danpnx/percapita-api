package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.models.Tag
import br.com.project.projetoIntegrador.service.TagService
import br.com.project.projetoIntegrador.utils.ContextUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@CrossOrigin
@RequestMapping("/tag")
class TagController @Autowired constructor(private val tagService: TagService) {

    @PostMapping("/create")
    fun createTag(@RequestBody tag: Tag): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        if(tagService.existsByTagNameAndUser(tag.tagName.toString(), username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe uma tag com este nome.")
        }
        tagService.registerTag(tag.tagName.toString(), username)
        return ResponseEntity.status(HttpStatus.CREATED).body("Tag "+ tag.tagName.toString() + " criada!")
    }

    @PutMapping("/edit/{id}")
    fun editTag(@PathVariable id: String,@RequestParam newName: String?): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        tagService.editTag(UUID.fromString(id), newName, username)
        return ResponseEntity.status(HttpStatus.CREATED).body("Nome da tag atualizada.")
    }

    @DeleteMapping("/delete/{id}")
    fun deleteTag(@PathVariable id: String): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        tagService.deleteTag(UUID.fromString(id), username)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/all")
    fun getAll(): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        return ResponseEntity.ok(tagService.getAllTags(username))
    }

    @GetMapping("/by-id")
    fun getTagById(@RequestParam tagId: String?): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        return ResponseEntity.ok(tagService.getTagById(UUID.fromString(tagId), username))
    }
}