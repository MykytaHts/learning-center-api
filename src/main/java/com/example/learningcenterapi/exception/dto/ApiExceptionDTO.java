package com.example.learningcenterapi.exception.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ApiExceptionDTO {
    private int code;
    private String message;
    private String stackTrace;
    private Map<String, Object> properties;

    public ApiExceptionDTO code(int code) {
        this.code = code;
        return this;
    }
}