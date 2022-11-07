package br.com.project.config;

import br.com.project.exceptions.*;
import br.com.project.models.StandardMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class CustomizedExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardMessage> badCredentials(BadCredentialsException e, HttpServletRequest request) {
        StandardMessage message = new StandardMessage(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro ao processar o dado",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(DataNotAvailableException.class)
    public ResponseEntity<StandardMessage> emailNotAvailable(DataNotAvailableException e, HttpServletRequest request) {
        StandardMessage message = new StandardMessage(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "Não foi possível realizar o cadastro",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @ExceptionHandler(RecoverPasswordTokenExpiredException.class)
    public ResponseEntity<StandardMessage> recoverPasswordTokenExpired(RecoverPasswordTokenExpiredException e, HttpServletRequest request) {
        StandardMessage message = new StandardMessage(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Token expirado",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(FailToLoginException.class)
    public ResponseEntity<StandardMessage> failToLogin(FailToLoginException e, HttpServletRequest request) {
        StandardMessage message = new StandardMessage(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Falha ao autenticar usuário",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardMessage> databaseException(DatabaseException e, HttpServletRequest request) {
        StandardMessage message = new StandardMessage(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Nenhum dado encontrado no banco",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<StandardMessage> invalidToken(InvalidTokenException e, HttpServletRequest request) {
        StandardMessage message = new StandardMessage(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Erro de validação de token de expiração",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }
}
