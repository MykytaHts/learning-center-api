package com.example.learningcenterapi.dto.response;

import com.example.learningcenterapi.domain.enumeration.TestCompletionStatus;
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
public class TestAttemptDTO implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long testId;
    private TestCompletionStatus status;
    private Double grade;
}
