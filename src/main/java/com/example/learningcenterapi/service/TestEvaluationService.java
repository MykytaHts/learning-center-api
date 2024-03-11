package com.example.learningcenterapi.service;

import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.dto.response.TestAttemptDTO;
import com.example.learningcenterapi.dto.request.TestResultDTO;

/**
 * Service Interface for managing {@link com.example.learningcenterapi.domain.TestAttempt} entity.
 */
public interface TestEvaluationService {
    /**
     * Saves the evaluation result of a test attempt.
     *
     * @param testId The ID of the test.
     * @param testResult The result of the test evaluation.
     * @param user The user who attempted the test.
     * @return The saved TestAttemptDTO representing the evaluation result.
     */
    TestAttemptDTO saveTestEvaluationResult(Long testId, TestResultDTO testResult, User user);

    /**
     * Finds a TestAttemptDTO by test ID and user ID.
     *
     * @param testId the ID of the test
     * @param userId the ID of the user
     * @return the TestAttemptDTO object representing the found test attempt,
     *         or null if no test attempt is found with the given test ID and user ID
     */
    TestAttemptDTO findAttemptByTestAndStudent(Long testId, Long userId);
}
