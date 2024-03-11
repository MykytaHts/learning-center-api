package com.example.learningcenterapi.service;


import com.example.learningcenterapi.domain.Submission;
import com.example.learningcenterapi.dto.request.LessonEvaluationDTO;
import com.example.learningcenterapi.dto.response.SubmissionResponseDTO;

import java.util.List;

/**
 * Service Interface for managing {@link Submission} entity.
 */
public interface SubmissionService {
    /**
     * Finds a {@link SubmissionResponseDTO} by the given lessonId and studentId.
     *
     * @param lessonId   the ID of the lesson
     * @param studentId  the ID of the student
     * @return the SubmissionResponseDTO with the corresponding lessonId and studentId
     */
    SubmissionResponseDTO findByLessonAndStudent(Long lessonId, Long studentId);

    /**
     * Returns a list of {@link SubmissionResponseDTO} objects representing all submissions for a given lesson.
     *
     * @param lessonId The ID of the lesson.
     * @return A list of {@link SubmissionResponseDTO} objects.
     */
    List<SubmissionResponseDTO> findAllByLessonId(Long lessonId);

    /**
     * Retrieves a list of SubmissionResponseDTO objects by student ID and current user ID.
     *
     * @param studentId      the ID of the student
     * @return a list of SubmissionResponseDTO objects
     */
    List<SubmissionResponseDTO> findAllByStudentId(Long studentId);

    /**
     * Retrieves a list of submissions based on the given student ID and course ID.
     *
     * @param studentId The ID of the student.
     * @param courseId The ID of the course.
     * @return A list of submissions matching the student ID and course ID.
     */
    List<SubmissionResponseDTO> findAllByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * Saves a homework evaluation for a lesson and a student.
     *
     * @param homeworkEvaluation The evaluation of the homework.
     * @param lessonId          The ID of the lesson.
     * @param studentId         The ID of the student.
     * @return The response DTO containing the saved submission information.
     */
    SubmissionResponseDTO save(LessonEvaluationDTO homeworkEvaluation, Long lessonId, Long studentId);

    /**
     * Updates the homework evaluation for a specific lesson and student.
     *
     * @param homeworkEvaluation The homework evaluation to update
     * @param lessonId           The ID of the lesson
     * @param studentId          The ID of the student
     * @return The updated submission response DTO
     */
    SubmissionResponseDTO update(LessonEvaluationDTO homeworkEvaluation, Long lessonId, Long studentId);

    /**
     * Deletes a submission for a specified lesson and student.
     *
     * @param lessonId  The ID of the lesson.
     * @param studentId The ID of the student.
     */
    void delete(Long lessonId, Long studentId);
}
