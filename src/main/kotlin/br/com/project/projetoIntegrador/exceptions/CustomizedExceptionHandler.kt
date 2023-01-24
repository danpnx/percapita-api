package br.com.project.projetoIntegrador.exceptions

import br.com.project.projetoIntegrador.payload.StandardMessage
import jakarta.servlet.http.HttpServletRequest
import kotlinx.datetime.Clock
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class CustomizedExceptionHandler {

    @ExceptionHandler(AuthorizationException::class)
    fun authorizationException(e: AuthorizationException, request: HttpServletRequest): ResponseEntity<StandardMessage> {
        val message = StandardMessage(
            Clock.System.now(),
            HttpStatus.FORBIDDEN.value(),
            "Usuário não autorizado a acessar endpoint",
            e.message,
            request.requestURI)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun resourceNotFound(e: ResourceNotFoundException, request: HttpServletRequest): ResponseEntity<StandardMessage> {
        val message = StandardMessage(
            Clock.System.now(),
            HttpStatus.NOT_FOUND.value(),
            "Recurso não encontrado no banco de dados",
            e.message,
            request.requestURI)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message)
    }

    @ExceptionHandler(InvalidInputException::class)
    fun invalidInput(e: InvalidInputException, request: HttpServletRequest): ResponseEntity<StandardMessage> {
        val message = StandardMessage(
            Clock.System.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Campo inválido",
            e.message,
            request.requestURI)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }

    @ExceptionHandler(DataNotAvailableException::class)
    fun dataNotAvailable(e: DataNotAvailableException, request: HttpServletRequest): ResponseEntity<StandardMessage> {
        val message = StandardMessage(
            Clock.System.now(),
            HttpStatus.CONFLICT.value(),
            "Não foi possível finalizar a ação",
            e.message,
            request.requestURI)
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message)
    }

    @ExceptionHandler(RecoverPasswordTokenExpiredException::class)
    fun recoverPasswordTokenExpired(e: RecoverPasswordTokenExpiredException, request: HttpServletRequest): ResponseEntity<StandardMessage> {
        val message = StandardMessage(
            Clock.System.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Token expirado",
            e.message,
            request.requestURI)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }

    @ExceptionHandler(FailToLoginException::class)
    fun failToLogin(e: FailToLoginException, request: HttpServletRequest): ResponseEntity<StandardMessage> {
        val message = StandardMessage(
            Clock.System.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "Falha ao autenticar usuário",
            e.message,
            request.requestURI)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message)
    }

    @ExceptionHandler(DatabaseException::class)
    fun databaseException(e: DatabaseException, request: HttpServletRequest): ResponseEntity<StandardMessage> {
        val message = StandardMessage(
            Clock.System.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Nenhum dado encontrado no banco",
            e.message,
            request.requestURI)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }

    @ExceptionHandler(InvalidTokenException::class)
    fun invalidToken(e: InvalidTokenException, request: HttpServletRequest): ResponseEntity<StandardMessage> {
        val message = StandardMessage(
            Clock.System.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "Erro de validação de token de expiração",
            e.message,
            request.requestURI)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentials(e: BadCredentialsException, request: HttpServletRequest): ResponseEntity<StandardMessage> {
        val message = StandardMessage(
            Clock.System.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "Falha ao efetuar login",
            e.message,
            request.requestURI
        )

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message)
    }

    @ExceptionHandler(MessagingException::class)
    fun messagingException(e: MessagingException, request: HttpServletRequest): ResponseEntity<StandardMessage> {
        val message = StandardMessage(
            Clock.System.now(),
            HttpStatus.BAD_REQUEST.value(),
            error = "Falha ao enviar o e-mail",
            e.message,
            request.requestURI
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
    }

}