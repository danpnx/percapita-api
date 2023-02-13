package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.dto.EditPasswordDTO
import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.repositories.UserRepository
import br.com.project.projetoIntegrador.utils.InputUtils
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class EditProfileService @Autowired constructor(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder
    ) {

    fun editName(username: String, newName: String) {
        val user = userRepository.findByUsername(username).get()
        user.name = newName
        userRepository.save(user)
    }

    fun editPassword(username: String, editPasswordDTO: EditPasswordDTO) {
        val user = userRepository.findByUsername(username).get()
        if(user.password == editPasswordDTO.actualPassword) {
            if(InputUtils.validatePassword(editPasswordDTO.newPassword)) {
                user.password = passwordEncoder.encode(editPasswordDTO.newPassword)
                userRepository.save(user)
            } else throw InvalidInputException("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial")
        } else throw InvalidInputException("Senha atual inválida!")
    }
}