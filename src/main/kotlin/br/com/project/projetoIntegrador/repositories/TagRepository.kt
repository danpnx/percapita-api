package br.com.project.projetoIntegrador.repositories

import br.com.project.projetoIntegrador.models.Tag
import br.com.project.projetoIntegrador.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TagRepository : JpaRepository<Tag, UUID> {

    fun existsByTagNameContainingIgnoreCaseAndUser(tagName: String, user: User): Boolean
    fun findAllByUser(findById: Optional<User>): List<Tag>
    fun findByTagNameAndUser(tagName: String?, user: User): Tag
}