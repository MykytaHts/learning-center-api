package com.example.learningcenterapi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@ToString
@Builder
public class OptionResponseDTO implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long id;
    private String content;
    private boolean correct;
}
