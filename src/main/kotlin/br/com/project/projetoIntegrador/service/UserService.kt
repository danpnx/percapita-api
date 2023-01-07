package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.models.User
import br.com.project.projetoIntegrador.repositories.UserRepository
import br.com.project.projetoIntegrador.utils.InputUtils
import br.com.project.projetoIntegrador.utils.TokenUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.CreatedBy
import org.springframework.http.HttpStatus
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class UserService @Autowired constructor(private var userRepository: UserRepository,
                                         private var mailSender: JavaMailSender,
                                         private var encoder: PasswordEncoder
    ) {


    fun findByUsername(username: String): User {
        return userRepository.findByUsername(username)
    }

    fun setTokenAndUpdate(user: User, token: String?, time: LocalDateTime?) {
        if (token != null) {
            user.token = token
        }
        if (time != null) {
            user.tokenCreateTime = time
        }
        userRepository.save(user)
    }

    fun existsByUsername(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    fun sendEmail(username: String) {
        val message: SimpleMailMessage = SimpleMailMessage()
        val from: String = "suportepercapita@outlook.com"
        val subject: String = "Criação de conta percapita"
        val text: String = " Ficamos felizes em receber seu cadastro em nosso aplicativo de gestão financeira, Percapita! \n\n"
        " Para realizar o seu login, basta acessar nossa página de login: https://percapita.azurewebsites.net/login \n"

            message.from = from
            message.setTo(username)
            message.subject = subject
            message.text = text
            mailSender.send(message)
    }

    fun updatePassword(password: String, user: User) {
        if(InputUtils.validatePassword(password) || password.length < 8) {
            throw InvalidInputException("A senha deve conter de 8 a 20 caracteres, pelo menos um caractere em maiúsculo e um caractere especial")
        }
        if(InputUtils.isExceedingPasswordSize(password)) {
            throw InvalidInputException("A sua senha não deve exceder 20 caracteres")
        }

        user.password = password//encoder.encode(password)
        userRepository.save(user)

    }

    fun findByToken(token: String): Optional<User> {
        return userRepository.findByToken(token)
    }

    fun save(user: User): User {
        return userRepository.save(user)
    }

    fun setTokenAndUpdate(user: User) {
        user.token = TokenUtils.generateToken()
        user.tokenCreateTime = LocalDateTime.now()
        userRepository.save(user)
    }

    fun register(user: User): HttpStatus {
        if(existsByUsername(user.username.toString())) {
            return HttpStatus.CONFLICT
        }

        if(user.username.toString() == null || user.username.equals("")) {
            throw InvalidInputException("Digite um email válido")
        }

        if(InputUtils.isExceedingUsernameSize(user.username.toString())) {
            throw InvalidInputException("O email não deve possuir mais que 100 caracteres")
        }

        // Validando nome do usuário
        if(user.name.toString() == null || user.username.equals("")) {
            throw InvalidInputException("Digite o seu nome")
        }

        if(InputUtils.isExceedingCompleteNameSize(user.name.toString())) {
            throw InvalidInputException("O nome não deve exceder 60 caracteres")
        }

        // Validando senha do usuário
        if(user.password.toString() == null || user.password.equals("")) {
            throw InvalidInputException("A sua senha não deve exceder 20 caracteres")
        }

        if(!InputUtils.validatePassword(user.password.toString()) || user.password?.length!! < 8) {
            return HttpStatus.BAD_REQUEST
        }

        if(InputUtils.isExceedingPasswordSize(user.password.toString())) {
            throw InvalidInputException("A sua senha não deve exceder 20 caracteres")
        }

            val password: String = encoder.encode(user.password)
            user.password = password
            userRepository.save(user)
            sendEmail(user.username.toString())
            return HttpStatus.CREATED
    }
}