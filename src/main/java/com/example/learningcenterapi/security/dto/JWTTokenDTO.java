package com.example.learningcenterapi.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JWTTokenDTO {
    @NotBlank(message = "Token cannot be empty")
    private String token;
}
