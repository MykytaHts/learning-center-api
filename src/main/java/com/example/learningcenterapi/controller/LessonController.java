package com.example.learningcenterapi.controller;


import com.example.learningcenterapi.dto.request.LessonRequestDTO;
import com.example.learningcenterapi.dto.request.update.CourseUpdateDTO;
import com.example.learningcenterapi.dto.response.CourseResponseDTO;
import com.example.learningcenterapi.dto.response.LessonResponseDTO;
import com.example.learningcenterapi.dto.response.minimized.LessonMinimizedDTO;
import com.example.learningcenterapi.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The LessonController class handles the API endpoints related to lessons.
 */
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@RestController
public class LessonController {
    private final LessonService lessonService;

    /**
     * Retrieves all lessons for a given course.
     *
     * @param courseId The ID of the course.
     * @return A list of LessonResponseDTO objects representing the lessons found.
     */
    @PreAuthorize("@accessValidator.lessonAccessByCourseId(#courseId)")
    @GetMapping("/course/{courseId}/lessons")
    public List<LessonMinimizedDTO> getAllLessonsForCourse(@PathVariable Long courseId) {
        log.info("GET: Getting all Lessons.");
        return lessonService.findAllLessonsByCourseId(courseId);
    }

    /**
     * Retrieves a lesson by its ID.
     *
     * @param lessonId The ID of the lesson.
     * @return The ResponseEntity with LessonResponseDTO representing the lesson with the specified ID.
     */
    @PreAuthorize("@accessValidator.lessonAccessById(#lessonId)")
    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<LessonResponseDTO> getLessonById(@PathVariable Long lessonId) {
        log.info("GET method: Fetching Lesson by id: {}", lessonId);
        return ResponseEntity.ok(lessonService.findById(lessonId));
    }

    /**
     * Adds a lesson to a course.
     *
     * @param lessonRequestDTO the lesson details (title, content, index)
     * @param courseId the ID of the course to add the lesson to
     * @return the response DTO containing the added lesson details
     * @throws URISyntaxException if there is an error in creating the URI
     */
    @PreAuthorize("@accessValidator.modificationLessonAccessByCourseId(#courseId)")
    @PostMapping("/courses/{courseId}/lessons")
    public ResponseEntity<LessonResponseDTO> addLessonToCourse(
            @Valid @RequestBody LessonRequestDTO lessonRequestDTO, @PathVariable Long courseId)
            throws URISyntaxException {
        log.info("POST: Adding new Lesson with details: {} to the Course with id: {}", lessonRequestDTO, courseId);
        LessonResponseDTO lessonResponseDTO = lessonService.addToCourse(lessonRequestDTO, courseId);
        return ResponseEntity.created(new URI("/api/v1/lessons/" + lessonResponseDTO.getId() + ""))
                .body(lessonResponseDTO);
    }


    /**
     * Updates a lesson by its ID.
     *
     * @param lessonRequestDTO The DTO object containing the updated lesson details.
     * @param lessonId         The ID of the lesson to be updated.
     * @return The updated lesson as a {@link LessonResponseDTO} object.
     */
    @PreAuthorize("@accessValidator.modificationLessonAccessById(#lessonId)")
    @PutMapping("/lessons/{lessonId}")
    public ResponseEntity<LessonResponseDTO> updateLesson(
            @RequestBody @Valid LessonRequestDTO lessonRequestDTO, @PathVariable Long lessonId) {
        log.info("PUT: Updating Lesson with id: {}, new Lesson details: {}", lessonId, lessonRequestDTO);
        return ResponseEntity.ok(lessonService.updateById(lessonRequestDTO, lessonId));
    }

    /**
     * Updates the lessons for a course.
     *
     * @param lessons   The DTO containing the updated lessons.
     * @param courseId  The ID of the course.
     * @return The ResponseEntity with CourseResponseDTO as the body.
     */
    @PreAuthorize("@accessValidator.modificationLessonAccessByCourseId(#courseId)")
    @PutMapping("/courses/{courseId}/lessons")
    public ResponseEntity<CourseResponseDTO> updateLessonsForCourse(
            @RequestBody @Valid CourseUpdateDTO lessons, @PathVariable Long courseId) {
        log.info("PUT Request to update lessons for course with id: {}", courseId);
        lessonService.updateLessonsForCourse(lessons, courseId);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a lesson by its ID.
     *
     * @param lessonId The ID of the lesson to delete.
     * @return ResponseEntity<Void> indicating the success of the operation.
     */
    @PreAuthorize("@accessValidator.modificationLessonAccessById(#lessonId)")
    @DeleteMapping("/lessons/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        log.info("DELETE: Deleting Lesson with id: {}", lessonId);
        lessonService.deleteById(lessonId);
        return ResponseEntity.noContent().build();
    }
}