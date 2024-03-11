package com.example.learningcenterapi.service;

import com.example.learningcenterapi.dto.request.QuestionRequestDTO;
import com.example.learningcenterapi.dto.request.update.TestUpdateDTO;
import com.example.learningcenterapi.dto.response.QuestionResponseDTO;
import com.example.learningcenterapi.dto.response.TestResponseDTO;

public interface QuestionService {
    /**
     * Finds a question by its ID.
     *
     * @param questionId the ID of the question to find
     * @return the {@link QuestionResponseDTO} object representing the found question,
     *         or null if no question is found with the given ID
     */
    QuestionResponseDTO findById(Long questionId);

    /**
     * Updates a question with the given ID using the provided questionDTO.
     *
     * @param questionDTO The updated question data.
     * @param questionId The ID of the question to be updated.
     * @return The updated question response DTO.
     */
    QuestionResponseDTO updateById(QuestionRequestDTO questionDTO, Long questionId);

    /**
     * Update the questions for a test.
     *
     * @param testDTO   The updated test data including the new questions.
     * @param testId    The ID of the test to be updated.
     * @return The updated test response DTO with the new questions.
     */
    TestResponseDTO updateQuestionsForTest(TestUpdateDTO testDTO, Long testId);

    /**
     * Adds a question to a test.
     *
     * @param questionDTO The QuestionRequestDTO containing the details of the question to be added.
     * @param testId      The ID of the test.
     * @return The updated test response DTO with the new question.
     */
    TestResponseDTO addQuestionToTest(QuestionRequestDTO questionDTO, Long testId);

    /**
     * Deletes a question by its ID.
     *
     * @param questionId the ID of the question to delete
     */
    void deleteById(Long questionId);
}
