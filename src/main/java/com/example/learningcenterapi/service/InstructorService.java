package com.example.learningcenterapi.service;

import com.example.learningcenterapi.domain.Instructor;

/**
 * Service for Instructor entity.
 */
public interface InstructorService {
    /**
     * Finds an Instructor by their ID.
     *
     * @param id the ID of the Instructor
     * @return the Instructor object corresponding to the ID, or null if not found
     */
    Instructor findById(final Long id);
}
