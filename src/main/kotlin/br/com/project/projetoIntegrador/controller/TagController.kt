package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.models.Tag
import br.com.project.projetoIntegrador.payload.StandardMessage
import br.com.project.projetoIntegrador.service.TagService
import br.com.project.projetoIntegrador.utils.ContextUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@CrossOrigin
@RequestMapping("/tag")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "Endpoints para manipulação de tags")
@SecurityRequirement(name = "Bearer Authentication")
class TagController @Autowired constructor(private val tagService: TagService) {

    @Operation(
        summary = "Cadastrar tag",
        description = "Endpoint para cadastro de tag",
        tags = ["Tag"],
        responses = [
            ApiResponse(
                description = "CREATED",
                responseCode = "201",
                content = [
                    Content(schema = Schema(implementation = String::class))
                ]
            ),
            ApiResponse(
                description = "CONFLICT",
                responseCode = "409",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @PostMapping("/create")
    fun createTag(@RequestBody @Valid tag: Tag?): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        println("Username: $username")
        val response = tagService.registerTag(tag?.tagName.toString(), username)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @Operation(
        summary = "Editar tag",
        description = "Endpoint para edição de tag",
        tags = ["Tag"],
        responses = [
            ApiResponse(
                description = "CREATED",
                responseCode = "201",
                content = [
                    Content(schema = Schema(implementation = String::class))
                ]
            ),
            ApiResponse(
                description = "CONFLICT",
                responseCode = "409",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @PutMapping("/edit/{id}")
    fun editTag(@PathVariable id: String, @RequestBody newName: Tag?): ResponseEntity<Any> { //tirado o @RequestParam para @RequestBody
        val username: String = ContextUtils.getUsername()
        tagService.editTag(UUID.fromString(id), newName?.tagName, username)
        return ResponseEntity.status(HttpStatus.CREATED).body("Nome da tag atualizada.")
    }

    @Operation(
        summary = "Detetar tag",
        description = "Endpoint para exclusão de tag",
        tags = ["Tag"],
        responses = [
            ApiResponse(
                description = "NO CONTENT",
                responseCode = "204",
                content = []
            ),
            ApiResponse(
                description = "NOT FOUND",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @DeleteMapping("/delete/{id}")
    fun deleteTag(@PathVariable id: String): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        tagService.deleteTag(UUID.fromString(id), username)
        return ResponseEntity.noContent().build()
    }

    @Operation(
        summary = "Tags do usuário",
        description = "Endpoint para buscar todas as tags do usuário",
        tags = ["Tag"],
        responses = [
            ApiResponse(
                description = "OK",
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = Tag::class))
                    )
                ]
            ),
            ApiResponse(
                description = "NOT FOUND",
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("/all")
    fun getAll(): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        val getAll = tagService.getAllTags(username)
        return ResponseEntity.status(HttpStatus.OK).body(getAll)
    }

    @Operation(
        summary = "Buscar tag",
        description = "Endpoint para buscar uma tag específica",
        tags = ["Tag"],
        responses = [
            ApiResponse(
                description = "OK",
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Tag::class)
                    )
                ]
            ),
            ApiResponse(
                description = "FORBIDDEN",
                responseCode = "403",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @GetMapping("/{id}")
    fun getTagById(@PathVariable id: String): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        return ResponseEntity.ok(tagService.getTagById(UUID.fromString(id), username))
    }
}