package com.example.learningcenterapi.service;

import com.example.learningcenterapi.dto.request.HomeworkRequestDTO;
import com.example.learningcenterapi.dto.response.HomeworkResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service Interface for managing {@link com.example.learningcenterapi.domain.Homework} entity.
 */
public interface HomeworkService {


    /**
     * Retrieves the file link for the homework submission.
     *
     * @param lessonId   The ID of the lesson for the homework.
     * @param studentId  The ID of the student who submitted the homework.
     * @param identifier The identifier for the homework.
     * @return The file link for the homework submission.
     */
    String getHomeworkFileTemporalLink(Long lessonId, Long studentId, String identifier);
    /**
     * Finds all homework responses for a specific user.
     *
     * @param id The ID of the user.
     * @return A list of HomeworkResponseDTO objects representing the homework responses for the user.
     */
    List<HomeworkResponseDTO> findByUserId(Long id);

    /**
     * Finds a homework by its ID.
     *
     * @param homeworkId the ID of the homework to find
     * @return the HomeworkResponseDTO representing the found homework, or null if not found
     */
    HomeworkResponseDTO findById(Long homeworkId);

    /**
     * Retrieves all homework responses for a specific user for a given course.
     *
     * @param id       The ID of the user.
     * @param courseId The ID of the course.
     * @return A list of HomeworkResponseDTO objects representing the homework responses for the user for the course.
     */
    List<HomeworkResponseDTO> findByUserAndCourseId(Long id, Long courseId);


    byte[] getHomeworkFile(Long lessonId, Long studentId, String identifier);

    /**
     * Updates a homework entry in the database with the specified homeworkId.
     *
     * @param homework   The updated homework data.
     * @param homeworkId The ID of the homework entry to update.
     * @return The updated homework entry as a HomeworkResponseDTO object.
     */
    HomeworkResponseDTO updateById(HomeworkRequestDTO homework, Long homeworkId);

    /**
     * Uploads a homework file for a specific lesson and user.
     *
     * @param file The multipart file representing the homework file to be uploaded.
     * @param studentId The user's id who is submitting the homework.
     * @param lessonId The ID of the lesson the homework is for.
     * @return The response DTO representing the uploaded homework file.
     */
    HomeworkResponseDTO uploadHomeworkFile(MultipartFile file, Long studentId, Long lessonId);

    /**
     * Deletes a homework by its ID.
     *
     * @param homeworkId the ID of the homework to delete
     */
    void deleteById(Long homeworkId);
}
