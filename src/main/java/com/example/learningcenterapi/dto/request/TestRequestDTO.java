package com.example.learningcenterapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRequestDTO {
    @Size(min = 2, max = 255, message = "Title should contain between 10 and 255 characters.")
    @NotBlank(message = "Title can not be empty.")
    private String title;

    @Size(min = 10, max = 255, message = "Description should contain between 10 and 255 characters.")
    @NotBlank(message = "Description can not be empty.")
    private String description;
}
