package com.example.learningcenterapi.service;

import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.dto.request.FeedbackRequestDTO;
import com.example.learningcenterapi.dto.response.FeedbackResponseDTO;

import java.util.List;

/**
 * The FeedbackService interface defines the methods for managing feedback.
 */
public interface FeedbackService {
    /**
     * Finds a feedback response by its ID.
     *
     * @param id The ID of the feedback response.
     * @return The identified feedback response.
     */
    FeedbackResponseDTO findById(Long id);

    /**
     * Retrieves all feedback responses for a specific course based on the course ID.
     *
     * @param courseId The ID of the course to retrieve feedback responses for
     * @return A list of FeedbackResponseDTO objects containing the feedback responses for the specified course
     */
    List<FeedbackResponseDTO> findAllByCourseId(Long courseId);

    /**
     * Saves the feedback provided by a user for a specific course.
     *
     * @param feedbackDTO The FeedbackRequestDTO object containing the feedback information.
     * @param id The User object representing the user submitting the feedback.
     * @param courseId The ID of the course for which the feedback is being submitted.
     * @return The FeedbackResponseDTO object containing the saved feedback details.
     */
    FeedbackResponseDTO save(FeedbackRequestDTO feedbackDTO, User id, Long courseId);

    /**
     * Updates the feedback with the specified feedbackId using the provided feedbackDTO.
     *
     * @param feedbackDTO The DTO containing the updated feedback.
     * @param feedbackId The ID of the feedback to be updated.
     * @return The updated FeedbackResponseDTO.
     */
    FeedbackResponseDTO updateById(FeedbackRequestDTO feedbackDTO, Long feedbackId);

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     */
    void deleteById(Long id);
}
