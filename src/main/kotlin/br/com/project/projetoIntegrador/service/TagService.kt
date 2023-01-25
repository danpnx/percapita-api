package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.controller.TagController
import br.com.project.projetoIntegrador.exceptions.AuthorizationException
import br.com.project.projetoIntegrador.exceptions.DataNotAvailableException
import br.com.project.projetoIntegrador.exceptions.DatabaseException
import br.com.project.projetoIntegrador.exceptions.ResourceNotFoundException
import br.com.project.projetoIntegrador.models.FinancialTransaction
import br.com.project.projetoIntegrador.models.Tag
import br.com.project.projetoIntegrador.models.User
import br.com.project.projetoIntegrador.repositories.FinancialTransactionRepository
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
                                        private val tagRepository: TagRepository,
                                        private val transactionRepository: FinancialTransactionRepository) {

    fun existsByTagNameAndUser(tagName: String?, username: String): Boolean {
        return tagRepository.existsByTagNameContainingIgnoreCaseAndUser(tagName, userRepository.findByUsername(username).get())
    }

    fun registerTag(tagName: String, username: String) {
        val user = userRepository.findByUsername(username)
        println(user.toString())

        if(existsByTagNameAndUser(tagName, username)) {
            throw  DataNotAvailableException("Você já possui uma tag com esse nome")
        }

//        val tag = Tag(id = null, tagName = tagName, user = user.get())
        val tag = Tag(tagName = tagName, user = user.get())
        tagRepository.save(tag)

//        val userOptional: Optional<User> = userRepository.findByUsername(username)
//        lateinit var tag: Tag
//
//        try {
//            if(existsByTagNameAndUser(tagName, username)) {
//                throw  DataNotAvailableException("Você já possui uma tag com esse nome")
//            }
//
//            if(userOptional.isPresent) {
//                val user: User = userOptional.get()
//                tag.tagName = tagName
//                tag.user = user
//                tagRepository.save(tag)
//            }
//
//        } catch (e: DataNotAvailableException) {
//            e.message
//        }
    }

    fun editTag(tagId: UUID, newName: String?, username: String) {
        val tagOptional: Optional<Tag> = tagRepository.findById(tagId)

        try {
            if(existsByTagNameAndUser(newName, username)) {
                throw DataNotAvailableException("Você já possui uma tag com esse nome")
            }

            if(tagOptional.isEmpty) {
                throw ResourceNotFoundException("Essa tag não existe")
            }

            val tag: Tag = tagOptional.get()
//            tag.tagName = newName
            tagRepository.save(tag)

        } catch (e: DataNotAvailableException) {
            e.message
        }
}

    fun deleteTag(tagId: UUID, username: String) {
        try {
            if(!tagRepository.existsById(tagId)) {
                throw ResourceNotFoundException("Essa tag não existe")
            }

            val tagOptional: Optional<Tag> = tagRepository.findById(tagId)

            if(tagOptional.get().tagName.equals("Unknown")) {
                throw DatabaseException("Impossível deletar esta tag")
            }

            val user: User = userRepository.findByUsername(username).get()
            val tag: Tag = tagRepository.findByTagNameAndUser(tagOptional.get().tagName, user)

            if(!tagRepository.existsByTagNameContainingIgnoreCaseAndUser("Unknown", user)) {
                lateinit var tagTemp: Tag
                tagTemp.tagName = "Unknown"
                tagTemp.user = user
                tagRepository.save(tagTemp)
            }

            for(transaction: FinancialTransaction in tag.transaction) {
                transaction.tag = tagRepository.findByTagNameAndUser("Unknown", user)
                transactionRepository.save(transaction)
            }

            tagRepository.deleteById(tagId)

        } catch(e: DatabaseException) {
            e.message
        }
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
        val tag: Tag = getTagById(tagId, username)

        tag.user.let {
            if(it?.username == username) {
                throw  AuthorizationException("Essa tag não pertence ao usuário autenticado")
            }
        }

//        if(!tag.user!!.username.equals(username)) {
//            throw  AuthorizationException("Essa tag não pertence ao usuário autenticado")
//        }

        if(tag.tagName.equals("Unknown")) {
            tag.add(linkTo(methodOn(TagController::class.java).getAll()).withSelfRel())
            return tag

        }
        tag.add(linkTo(methodOn(TagController::class.java).getAll()).withSelfRel())
        tag.add(linkTo(methodOn(TagController::class.java).deleteTag(tag.id.toString())).withSelfRel())
        tag.add(linkTo(methodOn(TagController::class.java).editTag(tag.id.toString(), null)).withSelfRel())
        tagRepository.save(tag)
        return tag
    }

    fun getTagByTagNameAndUser(tagName: String, user: User): Tag {
        return tagRepository.findByTagNameAndUser(tagName, user)
    }

    fun registerTagInFinancialTransaction(tagName: String, user: User): Tag {
        val tag = Tag(tagName = tagName, user = user)
//        lateinit var tag: Tag
//        tag.tagName = tagName
//        tag.user = user
        tagRepository.save(tag)
        return tag
    }
}