package br.com.project.exceptions;

public class RecoverPasswordTokenExpiredException extends RuntimeException{

    public RecoverPasswordTokenExpiredException(String message) {
        super(message);
    }
}
