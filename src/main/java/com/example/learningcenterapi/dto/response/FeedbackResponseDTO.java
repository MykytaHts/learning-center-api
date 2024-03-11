package com.example.learningcenterapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@SuperBuilder
@ToString
public class FeedbackResponseDTO extends BaseResponseDTO {
    private final Long id;
    private final Long studentId;
    private final String feedback;
}
