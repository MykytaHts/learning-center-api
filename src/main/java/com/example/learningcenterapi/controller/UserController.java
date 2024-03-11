package com.example.learningcenterapi.controller;

import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.dto.request.RoleRequestDTO;
import com.example.learningcenterapi.dto.request.UserRequestDTO;
import com.example.learningcenterapi.dto.response.UserResponseDTO;
import com.example.learningcenterapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@RestController
public class UserController {
    private final UserService userService;
    
    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return a ResponseEntity containing the UserResponseDTO of the retrieved user
     */
    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        log.info("GET request to retrieve user by id: {}", userId);
        return ResponseEntity.ok(userService.findById(userId));
    }

    /**
     * Get all users.
     * Retrieve all users from the user service.
     *
     * @return ResponseEntity<List<UserResponseDTO>> - The response entity containing the list of user response DTOs.
     */
    @GetMapping(value = "/users")
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        log.info("GET request to retrieve all users");
        return userService.findAllUsers(pageable);
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @param user the authenticated user
     * @return a ResponseEntity containing the UserResponseDTO of the authenticated user
     */
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN', 'INSTRUCTOR')")
    @GetMapping(value = "/users/me")
    public ResponseEntity<UserResponseDTO> getMyAccount(@AuthenticationPrincipal User user) {
        log.info("GET request to retrieve user by id: {}", user.getId());
        return ResponseEntity.ok(userService.mapToResponse(user));
    }

    /**
     * Retrieves all instructors.
     *
     * @return ResponseEntity containing a list of UserResponseDTO representing the instructors
     */
    @GetMapping(value = "/instructors")
    public Page<UserResponseDTO> getAllInstructors(Pageable pageable) {
        log.info("GET request to retrieve all instructors");
        return userService.findAllInstructors(pageable);
    }

    /**
     * Retrieves all the students from the system.
     *
     * @return A ResponseEntity containing a List of UserResponseDTO objects representing the students.
     */
    @GetMapping(value = "/students")
    public Page<UserResponseDTO> getAllStudents(Pageable pageable) {
        log.info("GET request to retrieve all students");
        return userService.findAllStudents(pageable);
    }

    /**
     * Retrieves a list of users by course.
     *
     * @param courseId the ID of the course to retrieve the users from
     * @return a ResponseEntity containing a list of UserResponseDTO objects representing the users
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @GetMapping(value = "/courses/{courseId}/students")
    public ResponseEntity<List<UserResponseDTO>> getUsersByCourse(@PathVariable Long courseId) {
        log.info("GET request to retrieve students by course id: {}", courseId);
        return ResponseEntity.ok(userService.findAllStudentsByCourseId(courseId));
    }

    /**
     * Retrieves the list of instructors for a given course.
     *
     * @param courseId The ID of the course to retrieve instructors for.
     * @return Response entity containing a list of UserResponseDTO objects representing the instructors.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'INSTRUCTOR')")
    @GetMapping(value = "/courses/{courseId}/instructors")
    public ResponseEntity<List<UserResponseDTO>> getInstructorsByCourse(@PathVariable Long courseId) {
        log.info("GET request to retrieve instructors by course id: {}", courseId);
        return ResponseEntity.ok(userService.findAllInstructorsByCourseId(courseId));
    }

    /**
     * Assigns a role for a user.
     *
     * @param role    the role to be assigned
     * @param userId  the ID of the user
     * @return a ResponseEntity containing UserResponseDTO object with the assigned role
     */
    @PatchMapping(value = "/users/{userId}")
    public ResponseEntity<UserResponseDTO> assignRoleForUser(
            @Valid @RequestBody RoleRequestDTO role, @PathVariable Long userId) {
        log.info("PATCH request to assign role: {} for user id: {}", role.getRole(), userId);
        return ResponseEntity.ok()
                .body(userService.assignRoleForUser(userId, role.getRole()));
    }

    /**
     * Deletes a user with the specified `userId`.
     * This method handles the DELETE request at the endpoint `/users/{userId}`.
     *
     * @param userDTO The request body containing the details of the user to be deleted.
     *                This parameter is annotated with `@Valid` to enable input validation.
     * @param userId  The unique identifier of the user to be deleted.
     *                This parameter is extracted from the path variable `{userId}`.
     * @return A `ResponseEntity<UserResponseDTO>` object containing the response body with
     *         the updated user details and the HTTP status code indicating the result of the operation.
     *         The response body is wrapped in a `ResponseEntity` object to allow customizing the response status.
     */
    @PutMapping(value = "/users/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Valid @RequestBody UserRequestDTO userDTO, @PathVariable Long userId) {
        log.info("PUT request to update user by id: {}", userId);
        return ResponseEntity.ok(userService.updateById(userDTO, userId));
    }

    /**
     * Updates the logged-in user's account with the provided user information.
     *
     * @param userDTO The UserRequestDTO object containing the updated user information.
     * @param user The authenticated User object representing the logged-in user.
     * @return A ResponseEntity containing the UserResponseDTO object with the updated user information.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'INSTRUCTOR')")
    @PutMapping(value = "/users/me")
    public ResponseEntity<UserResponseDTO> updateMyAccount(
            @Valid @RequestBody UserRequestDTO userDTO, @AuthenticationPrincipal User user) {
        log.info("PUT request to update user by id: {}", user.getId());
        return ResponseEntity.ok(userService.updateById(userDTO, user.getId()));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to be deleted.
     * @return A ResponseEntity with no content if the user was successfully deleted.
     */
    @DeleteMapping(value = "/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        log.info("DELETE request to delete user by id: {}", userId);
        userService.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}