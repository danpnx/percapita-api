package br.com.project.projetoIntegrador.service

import br.com.project.projetoIntegrador.exceptions.InvalidTokenException
import br.com.project.projetoIntegrador.exceptions.RecoverPasswordTokenExpiredException
import br.com.project.projetoIntegrador.models.User
import br.com.project.projetoIntegrador.utils.TokenUtils
import jakarta.mail.MessagingException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.util.Optional

@Service
class PasswordRecoveryService @Autowired constructor(private var mailsender: JavaMailSender,
                                                     private var userService: UserService) {

    fun forgotPassword(username: String) {
        val user: User = userService.findByUsername(username)
        return userService.setTokenAndUpdate(user)
    }

    fun resetPassword(token: String, passaword: String): String {
        val userOptional: Optional<User> = userService.findByToken(token)

        if (userOptional.isEmpty) {
            throw InvalidTokenException("Token inválido");
        }
        val tokenCreationDate: LocalDateTime = userOptional.get().tokenCreateTime

        if (TokenUtils.isTokenExpired(tokenCreationDate)) {
            throw RecoverPasswordTokenExpiredException("O Token para a recuperação de senha foi expirado");
        }
        userService.updatePassword(passaword, userOptional.get())
        userService.setTokenAndUpdate(userOptional.get(), null, null)
        return "Senha atualizada com sucesso."
    }

    fun sendEmail(username: String, response: String) {
        val user: User = userService.findByUsername(username)
        val mail: SimpleMailMessage = SimpleMailMessage()
        val subject: String = "Solicitação de recuperação de senha:"
        val text: String = "Olá, ${user.name}. Aqui está o link para resetar a senha: $response\n"
        "Caso não tenha solicitado, por favor, ignore este email"
        val emailPercapita: String = "suportepercapita@outlook.com"

        try {
            mail.from = emailPercapita
            mail.setTo(username)
            mail.subject = subject
            mail.text = text
            mailsender.send(mail)
        } catch(e: MessagingException) {
                throw RuntimeException(e)
        }
    }
}