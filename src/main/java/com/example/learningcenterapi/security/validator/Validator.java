package com.example.learningcenterapi.security.validator;


import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.exception.UserAccessRestrictedException;

import java.util.Collection;

/**
 * The Validator interface provides methods for checking user access to various resources.
 */
public interface Validator {
    /**
     * Returns the principal user or throws an AccessDeniedException if the principal user is not present in the Security Context.
     *
     * @return The principal user
     * @throws UserAccessRestrictedException if the current user is not present in the Security Context
     */
    User getUserCredentialsOrThrow();

    /**
     * Checks whether the current user has access to a specific course.
     *
     * @param courseId the ID of the course to check access for
     * @return {@code true} if the user has access to the course, {@code false} otherwise
     */
    boolean courseAccessById(Long courseId);

    /**
     * Checks if the current user has access to the given course based on the instructor IDs.
     *
     * @param instructorIds a collection of instructor IDs associated with the course
     * @return true if the current user has access to the course, false otherwise
     */
    boolean courseAccessByInstructorIds(Collection<Long> instructorIds);


    /**
     * Checks if the current user has access to the given course based on the student IDs.
     *
     * @param courseId the ID of the course to check access for
     * @return true if the current user has access to the course, false otherwise
     */
    boolean modificationLessonAccessByCourseId(Long courseId);

    /**
     * Checks if the user has access to a lesson based on the lesson ID.
     *
     * @param lessonId the ID of the lesson to check access for
     * @return true if the user has access to the lesson, false otherwise
     */
    boolean modificationLessonAccessById(Long lessonId);

    /**
     * Checks if the user has access to a test based on the test ID.
     *
     * @param testId the ID of the test
     * @return true if the user has access to the test, false otherwise
     */
    boolean testAccessById(Long testId);

    /**
     * Checks if the user has access to a test based on the lesson ID.
     *
     * @param testId the ID of the test
     * @return true if the user has access to the test, false otherwise
     */
    boolean testAccessByLessonId(Long testId);

    /**
     * Checks if the instructor has access to a test with the specified ID.
     *
     * @param testId The ID of the test to check access for.
     * @return true if the instructor has access to the test, false otherwise.
     */
    boolean modificationTestAccessById(Long testId);

    /**
     * Checks if the current user has access to provide feedback for a given course.
     *
     * @param courseId the ID of the course to check access for
     * @return true if the user has access to provide feedback, false otherwise
     */
    boolean feedbackAccessByCourseId(Long courseId);

    /**
     * Determines if the current user has access to the feedback with the specified ID.
     *
     * @param feedbackId The ID of the feedback.
     * @return {@code true} if the current user has access to the feedback, {@code false} otherwise.
     */
    boolean feedbackAccessById(Long feedbackId);

    /**
     * Checks if a student has access to a specific homework.
     *
     * @param homeworkId The ID of the homework.
     * @param studentId The ID of the student.
     * @return {@code true} if the student has access to the homework, {@code false} otherwise.
     */
    boolean homeworkAccess(Long homeworkId, Long studentId);

    /**
     * Checks if the user has access to a specific homework based on the homework ID.
     *
     * @param homeworkId the ID of the homework to check access for
     * @return true if the user has access to the homework, false otherwise
     */
    boolean homeworkAccessById(Long homeworkId);

    /**
     * Checks if the user has access to the lesson for the given course.
     *
     * @param courseId the ID of the course to check access for
     * @return true if the user has access, false otherwise
     */
    boolean lessonAccessByCourseId(Long courseId);

    /**
     * Checks if the user has access to the lesson with the specified ID.
     *
     * @param lessonId The ID of the lesson to check access for.
     * @return true if the user has access to the lesson, false otherwise.
     */
    boolean lessonAccessById(Long lessonId);

    /**
     * Checks if a user has access to the submission based on the lesson ID and student ID.
     *
     * @param lessonId The ID of the lesson.
     * @param studentId The ID of the student. Pass null if the user is an instructor or admin.
     * @return True if the user has access to the submission, false otherwise.
     * @throws UserAccessRestrictedException if the user does not have the necessary permissions.
     */
    boolean submissionAccess(Long lessonId, Long studentId);

    /**
     * Determines if the user has access to the submission of a lesson.
     * @param lessonId the ID of the lesson
     * @return true if the user has access, false otherwise
     */
    boolean submissionAccess(Long lessonId);

    /**
     * Checks if the current user has access to a specific question based on the question ID.
     *
     * @param questionId the ID of the question to check access for
     * @return true if the user has access to the question, false otherwise
     */
    boolean questionAccessById(Long questionId);

    /**
     * Checks if the user has modification access to a question based on the question ID.
     *
     * @param questionId the ID of the question to check access for
     * @return true if the user has modification access to the question, false otherwise
     * @throws UserAccessRestrictedException if the user does not have the necessary permissions
     */
    boolean modificationQuestionAccessById(Long questionId);
}
