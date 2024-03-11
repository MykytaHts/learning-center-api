package com.example.learningcenterapi.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResultDTO {
    @NotNull(message = "Question is not specified.")
    private Long questionId;

    @NotEmpty(message = "No answers are provided.")
    private Set<Long> optionIds;
}
