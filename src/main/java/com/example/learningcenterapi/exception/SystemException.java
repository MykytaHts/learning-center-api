package com.example.learningcenterapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class SystemException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private HttpStatus httpStatus;

    public SystemException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(String message) {
        super(message);
    }
}
