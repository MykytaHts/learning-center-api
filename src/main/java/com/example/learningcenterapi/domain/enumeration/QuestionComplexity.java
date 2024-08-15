package com.example.learningcenterapi.domain.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum QuestionComplexity {
    LOW(1, 1),
    MEDIUM(2, 2),
    HARD(3, 3);

    private final int index;
    private final int value;

    public static QuestionComplexity valueOf(int index) {
        return Arrays.stream(QuestionComplexity.values())
                .filter(t -> t.getIndex() == index)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Enum index is not valid."));
    }
}
