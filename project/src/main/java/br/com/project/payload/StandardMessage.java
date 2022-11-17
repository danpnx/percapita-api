package br.com.project.payload;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.Instant;

// Classe para representar o erro no formato Json no Postman

public class StandardMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT-3")
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

    public StandardMessage() {
    }

    public StandardMessage(Instant timestamp, Integer status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}