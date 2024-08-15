package com.example.learningcenterapi.controller;

import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.dto.request.FeedbackRequestDTO;
import com.example.learningcenterapi.dto.response.FeedbackResponseDTO;
import com.example.learningcenterapi.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller class for handling feedback-related operations.
 */
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@RestController
public class FeedbackController {

    public String returnValue () {
        return "value";
    }
    private final FeedbackService feedbackService;

    /**
     * Retrieves feedback by ID.
     *
     * @param feedbackId the ID of the feedback
     * @return the response entity containing the feedback
     */
    @GetMapping("/feedbacks/{feedbackId}")
    public ResponseEntity<FeedbackResponseDTO> getFeedback(@PathVariable Long feedbackId) {
        log.info("GET request to retrieve feedback by id: {}", feedbackId);
        return ResponseEntity.ok(feedbackService.findById(feedbackId));
    }

    /**
     * Retrieves feedback for a specific course.
     *
     * @param courseId The id of the course.
     * @return A ResponseEntity containing a list of FeedbackResponseDTO objects representing the feedback for the course.
     */
    @GetMapping("/courses/{courseId}/feedbacks")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByCourseId(@PathVariable Long courseId) {
        log.info("GET request to retrieve feedback by course id: {}", courseId);
        return ResponseEntity.ok(feedbackService.findAllByCourseId(courseId));
    }

    /**
     * Creates a new feedback for a course.
     *
     * @param feedbackDTO The feedback request data.
     * @param courseId The ID of the course.
     * @param user The authenticated user.
     * @return ResponseEntity<FeedbackResponseDTO> The response entity containing the created feedback.
     */
    @PreAuthorize("@accessValidator.feedbackAccessByCourseId(#courseId)")
    @PostMapping("/courses/{courseId}/feedbacks")
    public ResponseEntity<FeedbackResponseDTO> createFeedback(
            @RequestBody @Valid FeedbackRequestDTO feedbackDTO,
            @PathVariable Long courseId,
            @AuthenticationPrincipal User user) {
        log.info("POST request to create feedback for course with id: {}", courseId);
        return ResponseEntity.ok(feedbackService.save(feedbackDTO, user, courseId));
    }

    /**
     * Updates a feedback with the given ID.
     *
     * @param feedbackDTO  The FeedbackRequestDTO object containing the updated feedback data.
     * @param feedbackId   The ID of the feedback to be updated.
     * @return ResponseEntity containing the updated FeedbackResponseDTO object.
     * @throws URISyntaxException if the URI is syntactically incorrect.
     */
    @PreAuthorize("@accessValidator.feedbackAccessById(#feedbackId)")
    @PutMapping("/feedbacks/{feedbackId}")
    public ResponseEntity<FeedbackResponseDTO> updateFeedback(
            @RequestBody @Valid FeedbackRequestDTO feedbackDTO,
            @PathVariable Long feedbackId) throws URISyntaxException {
        log.info("PUT request to update feedback with id: {}", feedbackId);
        FeedbackResponseDTO resultDTO = feedbackService.updateById(feedbackDTO, feedbackId);
        return ResponseEntity
                .created(new URI("/api/feedbacks/" + resultDTO.getId()))
                .body(resultDTO);
    }

    /**
     * Deletes the feedback with the specified feedbackId.
     *
     * @param feedbackId the ID of the feedback to delete
     * @return a ResponseEntity with no content ("204 No Content") indicating successful deletion
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/feedbacks/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long feedbackId) {
        log.info("DELETE request to delete feedback with id: {}", feedbackId);
        feedbackService.deleteById(feedbackId);
        return ResponseEntity.noContent().build();
    }
}
