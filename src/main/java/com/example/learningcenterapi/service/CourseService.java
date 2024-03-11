package com.example.learningcenterapi.service;

import com.example.learningcenterapi.dto.response.CourseStatusDTO;
import com.example.learningcenterapi.dto.request.CourseRequestDTO;
import com.example.learningcenterapi.dto.response.CourseResponseDTO;
import com.example.learningcenterapi.dto.response.minimized.CourseMinimizedDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
/**
 * Service Interface for managing {@link com.example.learningcenterapi.domain.Course} entity.
 */
public interface CourseService {
    /**
     * Finds a course by its ID.
     *
     * @param courseId the ID of the course to find
     * @return the CourseResponseDTO representing the found course
     */
    CourseResponseDTO findById(Long courseId);

    /**
     * Retrieves all courses.
     *
     * @return a list of CourseResponseDTO objects representing the courses
     */
    Page<CourseMinimizedDTO> findAll(Pageable pageable);

    /**
     * Returns a list of courses associated with the given student ID.
     *
     * @param studentId the ID of the student
     * @return a list of CourseResponseDTO objects representing the courses associated with the student
     */
    List<CourseMinimizedDTO> findAllByStudentId(Long studentId);

    /**
     * Retrieves all courses associated with the specified userId ID.
     *
     * @param userId the ID of the instructor
     * @return a list of CourseResponseDTO objects representing the courses
     */
    List<CourseMinimizedDTO> findMyCourses(Long userId);

    /**
     * Retrieves all courses associated with the specified instructor ID.
     *
     * @param instructorId the ID of the instructor
     * @return a list of CourseResponseDTO objects representing the courses
     */
    List<CourseMinimizedDTO> findAllByInstructorId(Long instructorId);

    /**
     * Retrieves the status of a user's course enrollment.
     *
     * @param courseId   the ID of the course
     * @param studentId  the ID of the student
     * @return the CourseStatusDTO object containing the course status and final grade
     */
    CourseStatusDTO getUserCourseStatus(Long courseId, Long studentId);

    /**
     * Saves a course with the provided courseDTO.
     *
     * @param courseDTO the course request DTO containing the title, description, and instructor IDs
     * @return the saved course response DTO
     */
    CourseResponseDTO save(CourseRequestDTO courseDTO, Long userId);

    /**
     * Updates a course by its ID.
     *
     * @param courseDTO  the updated course information
     * @param courseId   the ID of the course to update
     * @return the updated course response data transfer object (DTO)
     */
    CourseResponseDTO updateById(CourseRequestDTO courseDTO, Long courseId);

    /**
     * Adds a user to a course.
     *
     * @param courseId the ID of the course to add the user to
     * @param instructorId   the ID of the user to be added to the course
     */
    void addInstructorToCourse(Long courseId, Long instructorId);

    /**
     * Adds a user to a course.
     *
     * @param courseId the ID of the course to add the user to
     * @param studentId   the ID of the user to be added to the course
     */
    void addStudentToCourse(Long courseId, Long studentId);
    /**
     * Deletes the course with the given ID.
     *
     * @param courseId the ID of the course to delete
     */
    void deleteById(Long courseId);

    /**
     * Deletes an instructor from a course.
     *
     * @param courseId     the ID of the course
     * @param instructorId the ID of the instructor to be deleted
     */
    void deleteInstructorFromCourse(Long courseId, Long instructorId);
}
