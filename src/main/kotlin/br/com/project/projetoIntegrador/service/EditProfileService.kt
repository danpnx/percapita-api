package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.models.User
import br.com.project.projetoIntegrador.repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
@Transactional
class EditProfileService @Autowired constructor(private var userRepository: UserRepository) {

    fun editName(username: String, newName: String): HttpStatus {
        val user: User = userRepository.findByUsername(username)
        if(user == null) {
            throw InvalidInputException("teste")
            return HttpStatus.BAD_REQUEST
        }

        user.name = newName
        userRepository.save(user)
        return HttpStatus.CREATED
    }

    fun editPassword(username: String, newPassword: String): HttpStatus {
        val user: User = userRepository.findByUsername(username)
        if(user != null) {
            user.password = newPassword
            userRepository.save(user)
            return HttpStatus.CREATED
        }
        return HttpStatus.BAD_REQUEST
    }
}