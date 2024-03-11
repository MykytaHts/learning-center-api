package com.example.learningcenterapi.service;

;
import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.domain.enumeration.Role;
import com.example.learningcenterapi.dto.request.UserRequestDTO;
import com.example.learningcenterapi.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing {@link com.example.learningcenterapi.domain.User} entity.
 */
public interface UserService {
    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user to find.
     * @return The UserResponseDTO representing the found user, or null if no user was found.
     */
    UserResponseDTO findById(Long id);

    /**
     * Retrieves all the instructors.
     *
     * @return a list of UserResponseDTO objects representing the instructors
     */
    Page<UserResponseDTO> findAllInstructors(Pageable pageable);

    /**
     * Retrieves all the students.
     *
     * @return A list of UserResponseDTO objects representing the students.
     */
    Page<UserResponseDTO> findAllStudents(Pageable pageable);

    /**
     * Retrieves all users.
     *
     * @return A list of UserResponseDTO objects representing the users.
     */
    Page<UserResponseDTO> findAllUsers(Pageable pageable);

    /**
     * Retrieves all instructors associated with a specific course.
     *
     * @param courseId The ID of the course.
     * @return A list of {@link UserResponseDTO} objects representing the instructors.
     */
    List<UserResponseDTO> findAllInstructorsByCourseId(Long courseId);

    /**
     * Finds all the students associated with a given course ID.
     *
     * @param courseId The ID of the course.
     * @return A list of UserResponseDTO objects representing the students enrolled in the course.
     */
    List<UserResponseDTO> findAllStudentsByCourseId(Long courseId);

    /**
     * Saves a user.
     *
     * @param user the user to save
     * @return the user response DTO containing the saved user information
     */
    UserResponseDTO save(UserRequestDTO user);

    /**
     * Updates a user record in the database with the provided information.
     *
     * @param user The updated user information.
     * @param id   The ID of the user to update.
     * @return The updated user information.
     */
    UserResponseDTO updateById(UserRequestDTO user, Long id);

    /**
     * Assigns a role for a user identified by the given userId.
     *
     * @param userId The ID of the user to assign the role to.
     * @param role   The role to assign to the user.
     * @return The UserResponseDTO object representing the user with the assigned role.
     */
    UserResponseDTO assignRoleForUser(Long userId, Role role);

    /**
     * Maps a user to a UserResponseDTO.
     *
     * @param user The user to map.
     * @return The UserResponseDTO object representing the user.
     */
    UserResponseDTO mapToResponse(User user);

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to be deleted
     */
    void deleteById(Long id);
}
