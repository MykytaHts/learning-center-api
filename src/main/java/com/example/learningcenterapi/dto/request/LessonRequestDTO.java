package com.example.learningcenterapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode(of = {"title", "content"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonRequestDTO {
    private Long id;

    @Size(min = 2, max = 255, message = "Title should contain between 1 and 255 characters.")
    @NotBlank(message = "Title can not be empty.")
    private String title;

    @NotBlank(message = "Content can not be empty.")
    private String content;

    @Min(value = 1, message = "The lowest allowed index value is 1.")
    @NotNull(message = "Index of the lesson is not specified.")
    private Integer orderIndex;
}
