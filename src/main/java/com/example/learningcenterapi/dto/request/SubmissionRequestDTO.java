package com.example.learningcenterapi.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionRequestDTO {
    @NotNull(message = "Student is not specified.")
    private Long studentId;

    @NotNull(message = "Lesson is not specified.")
    private Long lessonId;

    @DecimalMin(value = "0.0", message = "Grade should be bigger than 0.0")
    @DecimalMax(value = "100.0", message = "Grade should be lower than 100.0")
    private Double grade;
}
