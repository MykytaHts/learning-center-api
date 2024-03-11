package com.example.learningcenterapi.dto.response;

import com.example.learningcenterapi.dto.response.minimized.LessonMinimizedDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(of = {"id", "description", "title"})
public class CourseResponseDTO extends BaseResponseDTO {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String description;
    private Long testId;
    private Set<LessonMinimizedDTO> lessons;
}
