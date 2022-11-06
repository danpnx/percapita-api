package br.com.project.exceptions;

// Exceção utilizada quando o usuário entrar com valores inválidos de login ou alteração de senha
// Usada quando há possibilidade de ser lançada EmptyResultDataAccessException
public class BadCredentialsException extends RuntimeException{

    public BadCredentialsException(String message) {
        super(message);
    }
}
