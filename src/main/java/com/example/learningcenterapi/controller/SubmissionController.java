package com.example.learningcenterapi.controller;

import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.dto.request.LessonEvaluationDTO;
import com.example.learningcenterapi.dto.response.SubmissionResponseDTO;
import com.example.learningcenterapi.service.SubmissionService;
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

@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@RestController
public class SubmissionController {
    private final SubmissionService submissionService;

    /**
     * Retrieves the submission for a given lesson and student.
     *
     * @param lessonId   the ID of the lesson
     * @param studentId  the ID of the student
     * @return the ResponseEntity containing the SubmissionResponseDTO
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/lessons/{lessonId}/students/{studentId}/submission")
    public ResponseEntity<SubmissionResponseDTO> getSubmission(
            @PathVariable Long lessonId, @PathVariable Long studentId) {
        log.info("GET Request to fetch submission for lesson with id: {}", lessonId);
        return ResponseEntity.ok(submissionService.findByLessonAndStudent(lessonId, studentId));
    }

    /**
     * Fetches all the submissions made by the authenticated user for a given course.
     *
     * @param courseId The ID of the course.
     * @param user     The authenticated user.
     * @return A ResponseEntity containing a list of SubmissionResponseDTO objects representing the submissions.
     */
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping(value = "/courses/{courseId}/submissions/my")
    public ResponseEntity<List<SubmissionResponseDTO>> getMySubmissionsByCourse(
            @PathVariable Long courseId, @AuthenticationPrincipal User user) {
        log.info("GET Request to fetch submissions for course with id: {}", courseId);
        return ResponseEntity.ok(submissionService.findAllByStudentIdAndCourseId(user.getId(), courseId));
    }

    @PreAuthorize("@accessValidator.submissionAccess(#lessonId)")
    @GetMapping(value = "/lessons/{lessonId}/submissions")
    public ResponseEntity<List<SubmissionResponseDTO>> getSubmissionsForLessonByStudent(@PathVariable Long lessonId) {
        log.info("GET Request to fetch submissions for lesson with id: {}", lessonId);
        return ResponseEntity.ok(submissionService.findAllByLessonId(lessonId));
    }

    /**
     * Retrieves the list of submissions made by a student.
     *
     * @param studentId the ID of the student
     * @return a ResponseEntity object with a List of SubmissionResponseDTOs representing the submissions made by the student
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/students/{studentId}/submissions")
    public ResponseEntity<List<SubmissionResponseDTO>> getSubmissionsByStudent(@PathVariable Long studentId) {
        log.info("GET Request to fetch submissions for student with id: {}", studentId);
        return ResponseEntity.ok(submissionService.findAllByStudentId(studentId));
    }

    /**
     * Retrieves the list of submissions made by a student for a specific course.
     *
     * @param courseId  the ID of the course
     * @param studentId the ID of the student
     * @return a ResponseEntity object with a List of SubmissionResponseDTOs representing the submissions made by the student
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/courses/{courseId}/students/{studentId}/submissions")
    public ResponseEntity<List<SubmissionResponseDTO>> getSubmissionsByCourseAndStudent(
            @PathVariable Long courseId, @PathVariable Long studentId) {
        log.info("GET Request to fetch submissions for student with id: {}", studentId);
        return ResponseEntity.ok(submissionService.findAllByStudentIdAndCourseId(courseId, studentId));
    }

    /**
     * Creates a submission for a given lesson and student.
     *
     * @param homeworkEvaluation The evaluation of the homework submitted for the lesson.
     *                           Must be a valid {@link LessonEvaluationDTO} object.
     * @param lessonId The ID of the lesson the submission is for.
     * @param studentId The ID of the student making the submission.
     * @return ResponseEntity<SubmissionResponseDTO> A response entity containing the created submission.
     * @throws URISyntaxException if a URI syntax exception occurs.
     *
     */
    @PreAuthorize("@accessValidator.submissionAccess(#lessonId)")
    @PostMapping(value = "/lessons/{lessonId}/students/{studentId}/submission")
    public ResponseEntity<SubmissionResponseDTO> createSubmission(
            @RequestBody @Valid LessonEvaluationDTO homeworkEvaluation,
            @PathVariable Long lessonId,
            @PathVariable Long studentId) throws URISyntaxException {
        log.info("POST Request to create submission for lesson with id: {}", lessonId);
        SubmissionResponseDTO submissionResponseDTO = submissionService.save(homeworkEvaluation, lessonId, studentId);
        return ResponseEntity
                .created(new URI("/api/v1/lessons/" + lessonId + "/students/" + studentId + "/submissions"))
                .body(submissionResponseDTO);
    }

    /**
     * Updates the submission for a lesson by a student.
     *
     * @param homeworkEvaluation The updated evaluation for the homework. It should be a valid LessonEvaluationDTO object.
     * @param lessonId           The ID of the lesson.
     * @param studentId          The ID of the student.
     * @return The updated submission response with the evaluation. It returns a ResponseEntity object with a SubmissionResponseDTO as the body.
     */
    @PreAuthorize("@accessValidator.submissionAccess(#lessonId)")
    @PutMapping(value = "/lessons/{lessonId}/students/{studentId}/submission")
    public ResponseEntity<SubmissionResponseDTO> updateSubmission(
            @RequestBody @Valid LessonEvaluationDTO homeworkEvaluation,
            @PathVariable Long lessonId,
            @PathVariable Long studentId) {
        log.info("PUT Request to update submission for lesson with id: {}", lessonId);
        SubmissionResponseDTO submissionResponseDTO = submissionService.update(homeworkEvaluation, lessonId, studentId);
        return ResponseEntity.ok(submissionResponseDTO);
    }

    /**
     * Deletes a submission for a lesson and student.
     * Requires authorization to access the submission based on the lessonId and studentId.
     *
     * @param lessonId  the ID of the lesson
     * @param studentId the ID of the student
     * @return a ResponseEntity with no content
     */
    @PreAuthorize("@accessValidator.submissionAccess(#lessonId)")
    @DeleteMapping(value = "/lessons/{lessonId}/students/{studentId}/submission")
    public ResponseEntity<Void> deleteSubmission(
            @PathVariable Long lessonId, @PathVariable Long studentId) {
        log.info("DELETE Request to delete submission for lesson with id: {}", lessonId);
        submissionService.delete(lessonId, studentId);
        return ResponseEntity.noContent().build();
    }
}