package com.example.learningcenterapi.controller;

import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.dto.response.TestAttemptDTO;
import com.example.learningcenterapi.dto.request.TestRequestDTO;
import com.example.learningcenterapi.dto.request.TestResultDTO;
import com.example.learningcenterapi.dto.response.TestResponseDTO;
import com.example.learningcenterapi.service.TestEvaluationService;
import com.example.learningcenterapi.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * This class is a controller for handling HTTP requests related to tests.
 */
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@RestController
public class TestController {
    private final TestService testService;
    private final TestEvaluationService testEvaluationService;

    /**
     * Retrieves a test by its ID.
     *
     * @param testId the ID of the test to retrieve
     * @return a ResponseEntity with the TestResponseDTO object representing the found test, or a ResponseEntity with an error status if no test is found with the given ID
     */
    @PreAuthorize("@accessValidator.testAccessById(#testId)")
    @GetMapping("/tests/{testId}")
    public ResponseEntity<TestResponseDTO> getTestById(@PathVariable Long testId) {
        log.info("GET request to retrieve test by id: {}", testId);
        return ResponseEntity.ok(testService.findById(testId));
    }

    /**
     * Completes a test and retrieves the results.
     *
     * @param testId the ID of the test
     * @param user the authenticated user
     * @return a ResponseEntity containing the TestAttemptDTO representing the results of the completed test
     */
    @PreAuthorize("hasRole('STUDENT') and @accessValidator.testAccessById(#testId)")
    @GetMapping("/tests/{testId}/result")
    public ResponseEntity<TestAttemptDTO> getTestResult(@PathVariable Long testId, @AuthenticationPrincipal User user) {
        log.info("Get request to receive results of passing the test id: {}", testId);
        TestAttemptDTO attempt = testEvaluationService.findAttemptByTestAndStudent(testId, user.getId());
        return ResponseEntity.ok()
                .body(attempt);
    }

    /**
     * Creates a new test using the provided testDTO.
     *
     * @param testDTO the TestRequestDTO object representing the test to be created
     * @return a ResponseEntity<TestResponseDTO> object with the HTTP status code and the created TestResponseDTO
     * @throws URISyntaxException if the URI syntax is invalid
     */
    @PreAuthorize("@accessValidator.modificationLessonAccessById(#lessonId)")
    @PostMapping("/lessons/{lessonId}/tests")
    public ResponseEntity<TestResponseDTO> createTest(
             @RequestBody @Valid TestRequestDTO testDTO, @PathVariable Long lessonId) throws URISyntaxException {
        log.info("POST request to create test with DTO: {} for lesson with id: {}", testDTO, lessonId);
        TestResponseDTO testResponseDTO = testService.assignToLesson(testDTO, lessonId);
        return ResponseEntity.created(new URI("/api/v1/tests/" + testResponseDTO.getId()))
                .body(testResponseDTO);
    }

    /**
     * Completes a test by saving the evaluation result.
     *
     * @param testResult The result of the test evaluation.
     * @param testId The ID of the test.
     * @param user The user who attempted the test.
     * @return The ResponseEntity containing the saved TestAttemptDTO representing the evaluation result.
     */
    @PreAuthorize("hasRole('STUDENT') and @accessValidator.testAccessById(#testId)")
    @PostMapping("/tests/{testId}/complete")
    public ResponseEntity<TestAttemptDTO> completeTest(
            @RequestBody @Valid TestResultDTO testResult,
            @PathVariable Long testId,
            @AuthenticationPrincipal User user) {
        log.info("POST request to complete test id: {} with result: {}", testId, testResult);
        TestAttemptDTO attempt = testEvaluationService.saveTestEvaluationResult(testId, testResult, user);
        return ResponseEntity.ok()
                .body(attempt);
    }

    /**
     * Updates a test with the given ID using the provided testDTO.
     *
     * @param testDTO The updated test data.
     * @param testId The ID of the test to be updated.
     * @return The updated test response DTO.
     */
    @PreAuthorize("@accessValidator.modificationTestAccessById(#testId)")
    @PutMapping("/tests/{testId}")
    public ResponseEntity<TestResponseDTO> updateTest(
            @RequestBody @Valid TestRequestDTO testDTO, @PathVariable Long testId) {
        log.info("PUT request to update test id: {} with DTO: {}", testId, testDTO);
        TestResponseDTO testResponseDTO = testService.updateById(testDTO, testId);
        return ResponseEntity.ok()
                .body(testResponseDTO);
    }

    /**
     * Deletes a test by its ID.
     *
     * @param testId the ID of the test to delete
     * @return a ResponseEntity representing the HTTP response
     */
    @PreAuthorize("@accessValidator.modificationTestAccessById(#testId)")
    @DeleteMapping("/tests/{testId}")
    public ResponseEntity<Void> deleteTestById(@PathVariable Long testId) {
        log.info("DELETE request to delete test by id: {}", testId);
        testService.deleteById(testId);
        return ResponseEntity.noContent().build();
    }
}