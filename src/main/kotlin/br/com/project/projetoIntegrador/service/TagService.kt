package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.exceptions.DataNotAvailableException
import br.com.project.projetoIntegrador.exceptions.DatabaseException
import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.exceptions.ResourceNotFoundException
import br.com.project.projetoIntegrador.models.Tag
import br.com.project.projetoIntegrador.models.User
import br.com.project.projetoIntegrador.repositories.FinancialTransactionRepository
import br.com.project.projetoIntegrador.repositories.TagRepository
import br.com.project.projetoIntegrador.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TagService @Autowired constructor(private val userRepository: UserRepository,
                                        private val tagRepository: TagRepository,
                                        private val transactionRepository: FinancialTransactionRepository) {

    fun existsByTagNameAndUser(tagName: String, username: String): Boolean {
        return tagRepository.existsByTagNameContainingIgnoreCaseAndUser(tagName, userRepository.findByUsername(username))
    }

    fun registerTag(tagName: String, username: String) {
        val userOptional: Optional<User> = userRepository.findByUsername(username)
        lateinit var tag: Tag

        if(existsByTagNameAndUser(tagName, username)) {
            throw  DataNotAvailableException("Você já possui uma tag com esse nome")
        }

        if(userOptional.isPresent) {
            val user: User = userOptional.get()
            tag.tagName = tagName
            tag.user = user
            tagRepository.save(tag)
        }
    }

    fun editTag(tagId: UUID, newName: String, username: String) {
        val tagOptional: Optional<Tag> = tagRepository.findById(tagId)

        if(existsByTagNameAndUser(newName, username)) {
            throw DataNotAvailableException("Você já possui uma tag com esse nome");
        }

        if(tagOptional.isEmpty) {
            throw ResourceNotFoundException("Essa tag não existe")
        }

        val tag: Tag = tagOptional.get();
        tag.tagName = newName
        tagRepository.save(tag);
}

    fun deleteTag(tagId: UUID, username: String) {
        val userOptional: Optional<User> = userRepository.findByUsername(username)
        if(userOptional.isPresent) {
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
            }
        }
        return list
    }

    fun getTagById(tagId: UUID, username: String): Tag {
        val tag: Optional<Tag> = tagRepository.findById(tagId)

    }

    fun getTagByTagNameAndUser(tagName: String, user: User): Tag {
        return tagRepository.findByTagNameAndUser(tagName, user)
    }

    fun registerTagInFinancialTransaction(tagName: String, user: User): Tag {
        lateinit var tag: Tag
        tag.tagName = tagName
        tag.user = user
        tagRepository.save(tag)
        return tag
    }
}