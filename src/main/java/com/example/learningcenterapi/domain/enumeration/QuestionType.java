package com.example.learningcenterapi.domain.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum QuestionType {
    MULTI_ANSWER(1),
    SINGLE_ANSWER(2),
    TEXT_ANSWER(3);

    private final int index;

    public static QuestionType valueOf(int index) {
        return Arrays.stream(QuestionType.values())
                .filter(t -> t.getIndex() == index)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Enum index is not valid."));
    }
}
