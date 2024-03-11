package com.example.learningcenterapi.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestResultDTO {
    @Size(min = 1, message = "No answers provided.")
    private List<QuestionResultDTO> questionResults;
}
