package com.example.learningcenterapi.domain.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CourseStatus {
    COMPLETED("Completed"),
    IN_PROGRESS("In progress"),
    FAILED("Failed");

    private final String title;
}
