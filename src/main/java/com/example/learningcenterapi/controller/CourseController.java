package com.example.learningcenterapi.controller;

import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.dto.response.CourseStatusDTO;
import com.example.learningcenterapi.dto.request.CourseRequestDTO;
import com.example.learningcenterapi.dto.response.CourseResponseDTO;
import com.example.learningcenterapi.dto.response.minimized.CourseMinimizedDTO;
import com.example.learningcenterapi.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The CourseController class handles the HTTP requests related to courses.
 *
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@RestController
public class CourseController {
    private final CourseService courseService;

    /**
     * Retrieves a course by its ID.
     *
     * @param courseId The ID of the course to retrieve.
     * @return A ResponseEntity containing the CourseResponseDTO representing the found course.
     */
    @PreAuthorize("@accessValidator.courseAccessById(#courseId)")
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CourseResponseDTO> getCourse(@PathVariable Long courseId) {
        log.info("GET Request to fetch course with id: {}", courseId);
        return ResponseEntity.ok()
                .body(courseService.findById(courseId));
    }

    /**
     * Retrieves all courses.
     *
     * @param pageable The pageable object containing the pagination information.
     * @return A Page object containing the CourseResponseDTO objects representing the courses.
     */
    @GetMapping("/courses")
    public Page<CourseMinimizedDTO> getAllCourses(Pageable pageable) {
        log.info("GET Request to fetch all courses");
        return courseService.findAll(pageable);
    }

    /**
     * Retrieves the status of a course for the authenticated user.
     *
     * @param courseId The ID of the course.
     * @param user The authenticated user.
     * @return A ResponseEntity containing the CourseStatusDTO representing the status of the course.
     */
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/courses/{courseId}/status")
    public ResponseEntity<CourseStatusDTO> getCourseStatus(
            @PathVariable Long courseId, @AuthenticationPrincipal User user) {
        log.info("GET Request to fetch status of course with id {} for user with id: {}", courseId, user.getId());
        return ResponseEntity.ok().body(courseService.getUserCourseStatus(courseId, user.getId()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/courses/{courseId}/students/{userId}/status")
    public ResponseEntity<CourseStatusDTO> getCourseStatusByUser(
            @PathVariable Long courseId, @PathVariable Long userId) {
        log.info("GET Request to fetch status of course with id {} for user with id: {}", courseId, userId);
        return ResponseEntity.ok().body(courseService.getUserCourseStatus(courseId, userId));
    }

    /**
     * Retrieves the courses associated with the authenticated user.
     *
     * @param user The authenticated user.
     * @return ResponseEntity<List<CourseResponseDTO>> The response entity containing the list of courses.
     */
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR')")
    @GetMapping("/courses/my")
    public ResponseEntity<List<CourseMinimizedDTO>> getMyCourses(@AuthenticationPrincipal User user) {
        log.info("POST Request to fetch courses of user with id: {}", user.getId());
        return ResponseEntity.ok().body(courseService.findMyCourses(user.getId()));
    }

    /**
     * Retrieves a list of courses associated with a specific instructor.
     *
     * @param instructorId the ID of the instructor to retrieve the courses for
     * @return a ResponseEntity containing a list of CourseResponseDTO objects representing the courses,
     *         or an empty list if no courses are found
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/instructors/{instructorId}/courses")
    public ResponseEntity<List<CourseMinimizedDTO>> getInstructorCourses(@PathVariable Long instructorId) {
        log.info("POST Request to fetch courses of user with id: {}", instructorId);
        return ResponseEntity.ok().body(courseService.findAllByInstructorId(instructorId));
    }

    /**
     * Returns the list of courses associated with a student.
     *
     * @param studentId The ID of the student.
     * @return A ResponseEntity containing a list of CourseResponseDTO objects that represents the courses associated with the student.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students/{studentId}/courses")
    public ResponseEntity<List<CourseMinimizedDTO>> getStudentCourses(@PathVariable Long studentId) {
        log.info("POST Request to fetch courses of student with id: {}", studentId);
        return ResponseEntity.ok().body(courseService.findAllByStudentId(studentId));
    }

    /**
     * Create a new course by saving the course information provided in the request body.
     *
     * @param courseDTO The course information to be created.
     * @param user The authenticated user who is creating the course.
     * @return The ResponseEntity containing the created CourseResponseDTO.
     * @throws URISyntaxException If the URI syntax is invalid.
     */
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PostMapping("/courses")
    public ResponseEntity<CourseResponseDTO> createCourse(
            @RequestBody @Valid CourseRequestDTO courseDTO,
            @AuthenticationPrincipal User user)
            throws URISyntaxException {
        log.info("POST Request to create a new course");
        CourseResponseDTO courseResponseDTO = courseService.save(courseDTO, user.getId());
        return ResponseEntity
                .created(new URI("/api/v1/tests/" + courseResponseDTO.getId()))
                .body(courseResponseDTO);
    }

    /**
     * Adds an instructor to a course.
     *
     * @param courseId The ID of the course to add the instructor to.
     * @param instructorId The ID of the instructor to be added.
     * @return An instance of ResponseEntity representing the HTTP response with no content.
     * This method requires the user to have an 'ADMIN' role.
     * This method maps to the POST method and the specified URL pattern.
     **/
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/courses/{courseId}/instructors/{instructorId}")
    public ResponseEntity<Void> addInstructorToCourse(@PathVariable Long courseId, @PathVariable Long instructorId) {
        log.info("POST Request to add instructor with id {} to course with id: {}", instructorId, courseId);
        courseService.addInstructorToCourse(courseId, instructorId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds a student to a course.
     *
     * @param courseId   the ID of the course to add the student to
     * @param userId   the ID of the student to be added
     * @return ResponseEntity<Void> representing the HTTP response with no content
     * The path in this case is "/courses/{courseId}/students/{studentId}" where {courseId} and {studentId} are placeholder variables.
     * Once the student is added to the course, a log message is created with the studentId and courseId.
     * Finally, a ResponseEntity with no content is returned to represent the successful execution of the method.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/courses/{courseId}/students/{userId}")
    public ResponseEntity<Void> addStudentToCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        log.info("POST Request to add student with id {} to course with id: {}", userId, courseId);
        courseService.addStudentToCourse(courseId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Subscribe the user to a course.
     *
     * @param courseId the ID of the course to subscribe to
     * @param user the authenticated user
     * @return a ResponseEntity with no content if the subscription was successful
     */
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/courses/{courseId}/students")
    public ResponseEntity<Void> subscribeToCourse(@PathVariable Long courseId, @AuthenticationPrincipal User user) {
        log.info("POST Request to add student with id {} to course with id: {}", user.getId(), courseId);
        courseService.addStudentToCourse(courseId, user.getId());
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the course with the given course ID.
     *
     * @param courseDTO A CourseRequestDTO object containing the updated course information.
     * @param courseId The ID of the course to be updated.
     * @return A ResponseEntity object with the updated CourseResponseDTO as the response body.
     * @throws org.springframework.security.access.AccessDeniedException If the access to update the course is denied.
     */
    @PreAuthorize("@accessValidator.courseAccessById(#courseId)")
    @PutMapping("/courses/{courseId}")
    public ResponseEntity<CourseResponseDTO> updateCourse(
            @RequestBody @Valid CourseRequestDTO courseDTO, @PathVariable Long courseId) {
        log.info("PUT Request to update course with id: {}", courseId);
        return ResponseEntity.ok()
                .body(courseService.updateById(courseDTO, courseId));
    }

    /**
     * Deletes an instructor from a course.
     *
     * @param courseId    The ID of the course.
     * @param instructorId The ID of the instructor.
     * @return A ResponseEntity indicating the success of the operation.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/courses/{courseId}/instructors/{instructorId}")
    public ResponseEntity<Void> deleteInstructorFromCourse(@PathVariable Long courseId, @PathVariable Long instructorId) {
        log.info("POST Request to add instructor with id {} to course with id: {}", instructorId, courseId);
        courseService.deleteInstructorFromCourse(courseId, instructorId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a course by its ID.
     *
     * @param courseId the unique identifier of the course to be deleted
     * @return a ResponseEntity with no content, indicating that the deletion was successful
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Void> deleteCourseById(@PathVariable Long courseId) {
        log.info("DELETE Request to delete course with id: {}", courseId);
        courseService.deleteById(courseId);
        return ResponseEntity.noContent().build();
    }
}