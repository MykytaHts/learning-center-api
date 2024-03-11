package com.example.learningcenterapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HomeworkResponseDTO extends BaseResponseDTO {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long id;
    private Long studentId;
    private Long lessonId;
    private String filePath;
}
