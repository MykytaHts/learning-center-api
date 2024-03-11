package com.example.learningcenterapi.exception;

import java.io.Serial;

public class UserAccessRestrictedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserAccessRestrictedException(String message) {
        super(message);
    }

    public UserAccessRestrictedException(String message, Throwable cause) {
        super(message, cause);
    }
}
