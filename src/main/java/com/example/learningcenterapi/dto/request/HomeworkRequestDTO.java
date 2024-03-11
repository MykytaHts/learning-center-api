package com.example.learningcenterapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeworkRequestDTO {
    @Size(min = 10, max = 255, message = "Homework description should contain between 10 and 255 characters.")
    @NotBlank(message = "Path to homework file is not specified.")
    private String filePath;
}
