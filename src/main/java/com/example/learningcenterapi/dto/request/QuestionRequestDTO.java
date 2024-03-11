package com.example.learningcenterapi.dto.request;

import com.example.learningcenterapi.domain.enumeration.QuestionComplexity;
import com.example.learningcenterapi.domain.enumeration.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionRequestDTO {
    private Long id;

    @Size(min = 5, max = 255, message = "Description should contain between 10 and 255 characters.")
    @NotBlank(message = "Description can not be empty.")
    private String description;

    @NotNull(message = "Question index is not specified.")
    private Integer orderIndex;

    @NotNull(message = "Question type is not specified.")
    private QuestionType questionType;

    @NotNull(message = "Question complexity is not specified.")
    private QuestionComplexity questionComplexity;

    @NotEmpty(message = "No options are provided.")
    private Set<OptionRequestDTO> options;
}
