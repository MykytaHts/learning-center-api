package com.example.learningcenterapi.security.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotNull(message = "First name is not specified")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotNull(message = "Last name is not specified")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank(message = "Everyone needs your email")
    @Email
    private String email;

    @NotBlank(message = "Password is not specified")
    @ToString.Exclude
    private String password;
}
