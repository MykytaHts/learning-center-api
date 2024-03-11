package com.example.learningcenterapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackRequestDTO {
    @NotBlank(message = "Feedback is not specified.")
    @Size(min = 10, max = 255, message = "Feedback should contain between 10 and 255 characters.")
    private String feedback;
}
