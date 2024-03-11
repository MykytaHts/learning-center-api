package com.example.learningcenterapi.service;

import com.example.learningcenterapi.dto.request.TestRequestDTO;
import com.example.learningcenterapi.dto.response.TestResponseDTO;

/**
 * Service Interface for managing {@link com.example.learningcenterapi.domain.Test} entity.
 */
public interface TestService {
    /**
     * Finds a test by its ID.
     *
     * @param testId the ID of the test to find
     * @return the {@link TestResponseDTO} object representing the found test,
     *         or null if no test is found with the given ID
     */
    TestResponseDTO findById(Long testId);

    /**
     * Saves a new test using the provided testDTO.
     *
     * @param testDTO The test data to save.
     * @return The saved test response DTO.
     */
    TestResponseDTO assignToLesson(TestRequestDTO testDTO, Long lessonId);

    /**
     * Updates a test with the given ID using the provided testDTO.
     *
     * @param testDTO The updated test data.
     * @param testId The ID of the test to be updated.
     * @return The updated test response DTO.
     */
    TestResponseDTO updateById(TestRequestDTO testDTO, Long testId);

    /**
     * Deletes a test by its ID.
     *
     * @param testId the ID of the test to delete
     */
    void deleteById(Long testId);
}
