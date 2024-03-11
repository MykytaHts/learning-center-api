package com.example.learningcenterapi.service;

import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.dto.request.HomeworkRequestDTO;
import com.example.learningcenterapi.dto.response.HomeworkResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service Interface for managing {@link com.example.learningcenterapi.domain.Homework} entity.
 */
public interface HomeworkService {
    /**
     * Retrieves the homework file for a given homework ID and user ID.
     *
     * @param homeworkId The ID of the homework.
     * @param userId     The ID of the user.
     * @return The homework file as a byte array.
     */
    byte[] getHomeworkFile(Long homeworkId, Long userId);

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
     * @param user The user who is submitting the homework.
     * @param lessonId The ID of the lesson the homework is for.
     * @return The response DTO representing the uploaded homework file.
     */
    HomeworkResponseDTO uploadHomeworkFile(MultipartFile file, User user, Long lessonId);

    /**
     * Deletes a homework by its ID.
     *
     * @param homeworkId the ID of the homework to delete
     */
    void deleteById(Long homeworkId);
}
