package com.example.learningcenterapi.service;

import com.example.learningcenterapi.dto.request.LessonRequestDTO;
import com.example.learningcenterapi.dto.request.update.CourseUpdateDTO;
import com.example.learningcenterapi.dto.response.LessonResponseDTO;
import com.example.learningcenterapi.dto.response.minimized.LessonMinimizedDTO;

import java.util.List;

/**
 * Service Interface for managing {@link com.example.learningcenterapi.domain.Lesson} entity.
 */
public interface LessonService {
    /**
     * Retrieves a list of lessons by course ID.
     *
     * @param courseId The ID of the course.
     * @return A list of LessonResponseDTO objects representing the lessons found.
     */
    List<LessonMinimizedDTO> findAllLessonsByCourseId(Long courseId);

    /**
     * Retrieves a Lesson by its id.
     *
     * @param id The id of the Lesson.
     * @return The LessonResponseDTO representing the Lesson with the specified id.
     */
    LessonResponseDTO findById(Long id);

    /**
     * Updates a lesson by its ID.
     *
     * @param lessonRequestDTO The DTO object containing the updated lesson details.
     * @param id               The ID of the lesson to be updated.
     * @return The updated lesson as a {@link LessonResponseDTO} object.
     */
    LessonResponseDTO updateById(LessonRequestDTO lessonRequestDTO, Long id);

    /**
     * Deletes a lesson by id.
     *
     * @param id The id of the lesson to delete.
     */
    void deleteById(Long id);

    /**
     * Adds a lesson to a course.
     *
     * @param lessonRequestDTO the lesson details (title, content, index)
     * @param courseId the ID of the course to add the lesson to
     * @return the response DTO containing the added lesson details
     */
    LessonResponseDTO addToCourse(LessonRequestDTO lessonRequestDTO, Long courseId);

    /**
     * Updates the lessons for a course.
     *
     * @param courseUpdateDTO The DTO containing the updated lessons.
     * @param courseId        The ID of the course.
     */
    void updateLessonsForCourse(CourseUpdateDTO courseUpdateDTO, Long courseId);
}
