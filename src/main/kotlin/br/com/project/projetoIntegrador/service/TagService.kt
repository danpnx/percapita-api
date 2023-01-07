package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.exceptions.InvalidInputException
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

    fun editTag(tagId: UUID, newName: String?, username: String) {

    }

    fun deleteTag(tagId: UUID, username: String) {

    }

    fun getAllTags(username: String): List<Tag> {

    }

    fun getTagById(fromString: UUID, username: String) {

    }

    fun registerTag(tagName: String, username: String) {
        try {
            val user: User = userRepository.findByUsername(username)
            lateinit var tag: Tag
            if(user != null) {
                tag.tagName = tagName
                tag.user = user
                tagRepository.save(tag)
            }
        } catch (e: InvalidInputException) {
            e.message
        }
    }

}