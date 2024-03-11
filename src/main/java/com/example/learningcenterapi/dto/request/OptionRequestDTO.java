package com.example.learningcenterapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionRequestDTO {
    @Size(min = 1, max = 255, message = "Option value should contain between 1 and 255 characters.")
    @NotBlank(message = "Option value is not specified.")
    private String content;

    @NotNull(message = "Specify whether option is correct or not.")
    private boolean correct;
}
