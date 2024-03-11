package com.example.learningcenterapi.dto.response.minimized;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonMinimizedDTO implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long id;
    private String title;
    private Integer orderIndex;
}
