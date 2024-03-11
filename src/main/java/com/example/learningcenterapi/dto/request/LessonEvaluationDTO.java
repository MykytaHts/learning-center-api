package com.example.learningcenterapi.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;


@Data
public class LessonEvaluationDTO {
    @DecimalMin(value = "0.0", message = "Grade should be bigger than 0.0")
    @DecimalMax(value = "100.0", message = "Grade should be lower than 100.0")
    private Double grade;
}
