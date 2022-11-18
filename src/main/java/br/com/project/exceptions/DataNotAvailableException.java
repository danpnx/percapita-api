package br.com.project.exceptions;

public class DataNotAvailableException extends RuntimeException{

    public DataNotAvailableException(String message) {
        super(message);
    }
}
