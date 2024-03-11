package com.example.learningcenterapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SubmissionResponseDTO extends BaseResponseDTO {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long studentId;
    private Long lessonId;
    private Double grade;
}
