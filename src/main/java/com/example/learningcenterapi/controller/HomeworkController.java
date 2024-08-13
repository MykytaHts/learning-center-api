package com.example.learningcenterapi.controller;

import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.dto.request.HomeworkRequestDTO;
import com.example.learningcenterapi.dto.response.HomeworkResponseDTO;
import com.example.learningcenterapi.service.HomeworkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@RestController
public class HomeworkController {
    private final HomeworkService homeworkService;

    /**
     * Retrieves all homeworks for a specific user.
     *
     * @param user The User object representing the user.
     * @return ResponseEntity object containing a list of HomeworkResponseDTO objects representing the homeworks for the user.
     */
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/homeworks/my")
    public ResponseEntity<List<HomeworkResponseDTO>> getMyHomeworks(@AuthenticationPrincipal User user) {
        log.info("GET Request to fetch all homeworks for specific user");
        return ResponseEntity.ok().body(homeworkService.findByUserId(user.getId()));
    }

    /**
     * Retrieves all homeworks for a specific user for a given course.
     *
     * @param courseId The ID of the course.
     * @param user The authenticated user.
     * @return A ResponseEntity containing a list of HomeworkResponseDTO.
     */
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/courses/{courseId}/homeworks/my")
    public ResponseEntity<List<HomeworkResponseDTO>> getMyHomeworksByCourse(
            @PathVariable Long courseId, @AuthenticationPrincipal User user) {
        log.info("GET Request to fetch all homeworks for specific user for course with id {}", courseId);
        return ResponseEntity.ok().body(homeworkService.findByUserAndCourseId(user.getId(), courseId));
    }


    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/lessons/{lessonId}/students/{studentId}/homework/{homeworkId}")
    public ResponseEntity<String> getMyHomeworkLink(
            @PathVariable Long lessonId,
            @PathVariable Long studentId,
            @PathVariable String homeworkId) {
        log.info("GET Request to fetch homework file with id {} for user {}", homeworkId, studentId);
        return ResponseEntity.ok()
                .body(homeworkService.getHomeworkFileTemporalLink(lessonId, studentId, homeworkId));
    }

    /**
     * Retrieves a homework by its ID.
     *
     * @param homeworkId the ID of the homework to retrieve
     * @return a ResponseEntity containing a HomeworkResponseDTO representing the found homework
     */
    @PostAuthorize("@accessValidator.homeworkAccess(returnObject.body.lessonId, returnObject.body.studentId)")
    @GetMapping("/homeworks/{homeworkId}")
    public ResponseEntity<HomeworkResponseDTO> getHomework(@PathVariable Long homeworkId) {
        log.info("GET Request to fetch homework with id {}", homeworkId);
        return ResponseEntity.ok().body(homeworkService.findById(homeworkId));
    }

    /**
     * Submits a homework file for a specific lesson.
     *
     * @param file      The multipart file representing the homework file to be submitted.
     * @param lessonId  The ID of the lesson the homework is for.
     * @param user      The user who is submitting the homework.
     * @return The ResponseEntity containing the response DTO representing the submitted homework file.
     * @throws URISyntaxException If there is an error in creating the URI for the created homework file.
     */
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/lessons/{lessonId}/homeworks")
    public ResponseEntity<HomeworkResponseDTO> submitHomework(
            @Valid MultipartFile file,
            @PathVariable Long lessonId,
            @AuthenticationPrincipal User user) throws URISyntaxException {
        log.info("POST Request to submit homework for lesson with id {}", lessonId);
        HomeworkResponseDTO homeworkDTO = homeworkService.uploadHomeworkFile(file, user.getId(), lessonId);
        return ResponseEntity.created(new URI("/api/v1/homeworks/" + homeworkDTO.getId()))
                .body(homeworkDTO);
    }


    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/homeworks/lesson/{lessonId}/student/{studentId}/{identifier}")
    public byte[] getHomeworkFile(@PathVariable final Long lessonId,
                                  @PathVariable final Long studentId,
                                  @PathVariable final String identifier) {
        log.debug("GET request to get a homework file for lesson id: {}, student id: {} and id : {}",
                lessonId, studentId, identifier);
        return homeworkService.getHomeworkFile(lessonId, studentId, identifier);
    }

    /**
     * Updates a homework entry in the database with the specified homeworkId.
     *
     * @param homeworkDTO The updated homework data.
     * @param homeworkId The ID of the homework entry to update.
     * @return The updated homework entry as a HomeworkResponseDTO object.
     */
    @PreAuthorize("@accessValidator.homeworkAccessById(#homeworkId)")
    @PutMapping("/homeworks/{homeworkId}")
    public ResponseEntity<HomeworkResponseDTO> updateHomework(
            @RequestBody @Valid HomeworkRequestDTO homeworkDTO, @PathVariable Long homeworkId) {
        log.info("PUT Request to update homework with id {}", homeworkId);
        return ResponseEntity.ok().body(homeworkService.updateById(homeworkDTO, homeworkId));
    }

    /**
     * Deletes a homework by its ID.
     *
     * @param homeworkId the ID of the homework to delete
     * @return a ResponseEntity with no content
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/homeworks/{homeworkId}")
    public ResponseEntity<Void> deleteHomework(@PathVariable Long homeworkId) {
        log.info("DELETE Request to delete homework with id {}", homeworkId);
        homeworkService.deleteById(homeworkId);
        return ResponseEntity.noContent().build();
    }
}