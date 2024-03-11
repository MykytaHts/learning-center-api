package com.example.learningcenterapi.dto.request.update;

import com.example.learningcenterapi.dto.request.QuestionRequestDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TestUpdateDTO {
    @NotEmpty(message = "No questions are provided.")
    private Set<QuestionRequestDTO> questions;
}
