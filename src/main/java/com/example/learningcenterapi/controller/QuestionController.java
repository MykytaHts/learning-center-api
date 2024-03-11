package com.example.learningcenterapi.controller;


import com.example.learningcenterapi.dto.request.QuestionRequestDTO;
import com.example.learningcenterapi.dto.request.update.TestUpdateDTO;
import com.example.learningcenterapi.dto.response.QuestionResponseDTO;
import com.example.learningcenterapi.dto.response.TestResponseDTO;
import com.example.learningcenterapi.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller class for handling questions-related API requests.
 */
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@RestController
public class QuestionController {
    private final QuestionService questionService;

    /**
     * Retrieves a question by its ID.
     *
     * @param questionId the ID of the question to retrieve
     * @return a ResponseEntity object with the retrieved question as the body
     */
    @PreAuthorize("@accessValidator.questionAccessById(#questionId)")
    @GetMapping("/questions/{questionId}")
    public ResponseEntity<QuestionResponseDTO> getQuestion(@PathVariable Long questionId) {
        log.info("GET request to retrieve question by id: {}", questionId);
        return ResponseEntity.ok(questionService.findById(questionId));
    }

    /**
     * Updates a question with the given ID using the provided questionDTO.
     *
     * @param questionDTO The updated question data.
     * @param questionId The ID of the question to be updated.
     * @return The updated question response DTO.
     */
    @PreAuthorize("@accessValidator.modificationQuestionAccessById(#questionId)")
    @PutMapping("/questions/{questionId}")
    public ResponseEntity<QuestionResponseDTO> updateQuestion(
            @RequestBody @Valid QuestionRequestDTO questionDTO, @PathVariable Long questionId) {
        log.info("PUT request to retrieve question by id: {}", questionId);
        return ResponseEntity.ok(questionService.updateById(questionDTO, questionId));
    }

    /**
     * Updates the questions of a test with the given TestUpdateDTO.
     * Only authorized users with modify access to the test can perform this operation.
     *
     * @param testDTO   the TestUpdateDTO containing the updated test questions
     * @param testId    the ID of the test
     * @return a ResponseEntity containing the updated TestResponseDTO
     * @throws URISyntaxException if there is an error creating the URI for the response
     */
    @PreAuthorize("@accessValidator.modificationTestAccessById(#testId)")
    @PutMapping("/tests/{testId}/questions")
    public ResponseEntity<TestResponseDTO> updateTestQuestions(
            @RequestBody @Valid TestUpdateDTO testDTO,
            @PathVariable Long testId) throws URISyntaxException {
        log.info("POST request to update test lessons id: {} with DTO: {}", testId, testDTO);
        TestResponseDTO testResponseDTO = questionService.updateQuestionsForTest(testDTO, testId);
        return ResponseEntity.created(new URI("/api/v1/tests/" + testId))
                .body(testResponseDTO);
    }

    /**
     * Adds a question to a test.
     *
     * @param questionDTO The QuestionRequestDTO containing the details of the question to be added.
     * @param testId      The ID of the test.
     * @return A ResponseEntity with the TestResponseDTO of the updated test.
     * @throws URISyntaxException If there is an error creating the URI for the response.
     */
    @PreAuthorize("@accessValidator.modificationTestAccessById(#testId)")
    @PostMapping("/tests/{testId}/questions")
    public ResponseEntity<TestResponseDTO> addQuestionToTest(
            @RequestBody @Valid QuestionRequestDTO questionDTO,
            @PathVariable Long testId) throws URISyntaxException {
        log.info("POST request to add question to test id: {} with DTO: {}", testId, questionDTO);
        TestResponseDTO testResponseDTO = questionService.addQuestionToTest(questionDTO, testId);
        return ResponseEntity.created(new URI("/api/v1/tests/" + testResponseDTO.getId()))
                .body(testResponseDTO);
    }

    /**
     * Deletes a question by its ID.
     *
     * @param questionId the ID of the question to delete
     * @return a {@link ResponseEntity} object with no content and a success status code (204)
     */
    @PreAuthorize("@accessValidator.modificationQuestionAccessById(#questionId)")
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        log.info("DELETE request to delete question by id: {}", questionId);
        questionService.deleteById(questionId);
        return ResponseEntity.noContent().build();
    }
}
