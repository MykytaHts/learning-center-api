package com.example.learningcenterapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TestResponseDTO extends BaseResponseDTO {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String description;
    private Long lessonId;
    private List<QuestionResponseDTO> questions;
}
