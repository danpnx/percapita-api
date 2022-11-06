package br.com.project.exceptions;

public class FailToLoginException extends RuntimeException{

    public FailToLoginException(String message) {
        super(message);
    }
}
