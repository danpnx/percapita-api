package br.com.project.payload;

import java.io.Serializable;

public class ReportResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private T responseValue;

    public ReportResponse() {
    }

    public void setResponseValue(T value) {
        this.responseValue = value;
    }

    public T getResponseValue() {
        return responseValue;
    }
}
