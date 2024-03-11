package com.example.learningcenterapi.service;

import com.example.learningcenterapi.domain.Student;

/**
 * Service for Student entity.
 */
public interface StudentService {
    /**
     * Find a Student by their ID.
     *
     * @param id The ID of the Student.
     * @return The Student object if found, otherwise null.
     */
    Student findById(final Long id);
}
