package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.controller.TagController
import br.com.project.projetoIntegrador.exceptions.*
import br.com.project.projetoIntegrador.models.Tag
import br.com.project.projetoIntegrador.models.User
import br.com.project.projetoIntegrador.repositories.TagRepository
import br.com.project.projetoIntegrador.repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class TagService @Autowired constructor(private val userRepository: UserRepository,
                                        private val tagRepository: TagRepository) {

    fun existsByTagNameAndUser(tagName: String?, username: String): Boolean = tagRepository.existsByTagNameContainingIgnoreCaseAndUser(
        tagName, userRepository.findByUsername(username).get())

    fun registerTag(tagName: String, username: String) {
        val user = userRepository.findByUsername(username)
        println(user.toString())

        if(existsByTagNameAndUser(tagName, username)) throw  DataNotAvailableException("Você já possui uma tag com esse nome")

        if(tagName.isBlank()) throw InvalidInputException("Digite um nome para sua tag")

        if(tagName.length < 3) throw InvalidInputException("A tag deve ter no minimo três caracteres")

        val tag = Tag(tagName = tagName, user = user.get())
        tagRepository.save(tag)
    }

    fun editTag(tagId: UUID, newName: String?, username: String) {
        val tagOptional: Optional<Tag> = tagRepository.findById(tagId)

        if(existsByTagNameAndUser(newName, username)) throw DataNotAvailableException("Você já possui uma tag com esse nome")

        if(newName.isNullOrBlank()) throw InvalidInputException("Digite um nome para sua tag")

        if(newName.length < 3) throw InvalidInputException("A tag deve ter no minimo três caracteres")

        if(tagOptional.isEmpty) throw ResourceNotFoundException("Essa tag não existe")

        val tag: Tag = tagOptional.get()
        tag.tagName = newName.toString()
        tagRepository.save(tag)
}

    fun deleteTag(tagId: UUID, username: String) {

        if(!tagRepository.existsById(tagId)) throw ResourceNotFoundException("Essa tag não existe")

        val tagOptional: Optional<Tag> = tagRepository.findById(tagId)

        if(tagOptional.get().transactions.isNotEmpty()) throw DatabaseException("Impossível deletar esta tag, pois existem trasações vinculadas à ela")

        tagRepository.deleteById(tagId)
    }

    fun getAllTags(username: String): List<Tag> {
        val userOptional: Optional<User> = userRepository.findByUsername(username)

        if(userOptional.isEmpty) {
            throw DatabaseException("Este usuário não existe")
        }

        val list: List<Tag> = tagRepository.findAllByUser(userOptional)

        if(list.isEmpty()) {
            throw  DatabaseException("Você não possui nenhuma tag cadastrada")
        }

        for(tag in list) {
            if(tag.tagName == "Unknown") {
                tag.add(linkTo(methodOn(TagController::class.java).getTagById(tag.id.toString())).withSelfRel())
                tag.add(linkTo(methodOn(TagController::class.java).createTag(null)).withSelfRel())
                continue
            }
            tag.add(linkTo(methodOn(TagController::class.java).getTagById(tag.id.toString())
                ).withSelfRel())
            tag.add(linkTo(methodOn(TagController::class.java).editTag(tag.id.toString(), null)
                ).withSelfRel())
            tag.add(linkTo(methodOn(TagController::class.java).deleteTag(tag.id.toString())
                ).withSelfRel())
            tag.add(linkTo(methodOn(TagController::class.java).createTag(null)).withSelfRel())
        }
        return list
    }

    fun getTagById(tagId: UUID, username: String): Tag {
        val tag: Tag = tagRepository.getReferenceById(tagId)

        if(tag.user?.username != username) throw DatabaseException("Essa tag nao pertence ao usuário autenticado")

        tag.add(linkTo(methodOn(TagController::class.java).getAll()).withSelfRel())
        tag.add(linkTo(methodOn(TagController::class.java).deleteTag(tag.id.toString())).withSelfRel())
        tag.add(linkTo(methodOn(TagController::class.java).editTag(tag.id.toString(), null)).withSelfRel())
        tagRepository.save(tag)
        return tag
    }

    fun getTagByTagNameAndUser(tagName: String, user: User): Tag = tagRepository.findByTagNameAndUser(tagName, user)

    fun registerTagInFinancialTransaction(tagName: String, user: User): Tag {
        val tag = Tag(tagName = tagName, user = user)
        tagRepository.save(tag)
        return tag
    }
}