package com.example.learningcenterapi.dto.request.update;

import com.example.learningcenterapi.dto.request.LessonRequestDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class CourseUpdateDTO {
    @NotEmpty(message = "No lessons are provided.")
    private Set<LessonRequestDTO> lessons;
}
