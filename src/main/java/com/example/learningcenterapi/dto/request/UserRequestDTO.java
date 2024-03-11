package com.example.learningcenterapi.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {

    @Size(min = 2, max = 30, message = "First name size has to contain between 2 and 20 characters.")
    @NotBlank(message = "First name can not be empty.")
    private String firstName;

    @Size(min = 2, max = 30, message = "Last name size has to contain between 2 and 20 characters.")
    @NotBlank(message = "Last name can not be empty.")
    private String lastName;

    @Email(message = "Email is not valid.")
    @NotBlank(message = "Email can not be empty.")
    private String email;
}
