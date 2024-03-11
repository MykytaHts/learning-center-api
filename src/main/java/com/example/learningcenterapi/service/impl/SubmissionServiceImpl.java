package com.example.learningcenterapi.service.impl;

import com.example.learningcenterapi.domain.*;
import com.example.learningcenterapi.domain.enumeration.TestCompletionStatus;
import com.example.learningcenterapi.dto.request.LessonEvaluationDTO;
import com.example.learningcenterapi.dto.response.SubmissionResponseDTO;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.mapper.SubmissionMapper;
import com.example.learningcenterapi.repository.LessonRepository;
import com.example.learningcenterapi.repository.StudentRepository;
import com.example.learningcenterapi.repository.SubmissionRepository;
import com.example.learningcenterapi.service.StudentService;
import com.example.learningcenterapi.service.SubmissionService;
import com.example.learningcenterapi.service.TestEvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.learningcenterapi.domain.enumeration.TestCompletionStatus.PASSED;
import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final StudentService studentService;
    private final TestEvaluationService testEvaluationService;
    private final SubmissionMapper submissionMapper;

    @Override
    @Transactional(readOnly = true)
    public SubmissionResponseDTO findByLessonAndStudent(Long lessonId, Long studentId) {
        log.info("Finding submission with lesson id: {} and student id: {}", lessonId, studentId);
        Submission submission = findEntityById(lessonId, studentId);
        return submissionMapper.toResponseDTO(submission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubmissionResponseDTO> findAllByLessonId(Long lessonId) {
        log.info("Finding all submissions by lesson id: {}", lessonId);
        checkNull(lessonId, "Lesson id");
        List<Submission> submissions = submissionRepository.findAllByLessonId(lessonId);
        return submissionMapper.toResponseDTOList(submissions);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubmissionResponseDTO> findAllByStudentId(Long studentId) {
        log.info("Finding all submissions by student id: {}", studentId);
        checkNull(studentId, "Student id");
        List<Submission> submissions = submissionRepository.findAllByStudentId(studentId);
        return submissionMapper.toResponseDTOList(submissions);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubmissionResponseDTO> findAllByStudentIdAndCourseId(Long studentId, Long courseId) {
        log.info("Finding all submissions by student id: {} and course id: {}", studentId, courseId);
        checkNull(studentId, courseId, "Student id", "Course id");
        List<Submission> submissions = submissionRepository.findAllByStudentIdAndLessonCourseId(studentId, courseId);
        return submissionMapper.toResponseDTOList(submissions);
    }

    @Override
    public SubmissionResponseDTO save(LessonEvaluationDTO homeworkEvaluation, Long lessonId, Long studentId) {
        log.info("Saving submission with lesson id: {} and student id: {}", lessonId, studentId);
        Lesson lesson = findLessonById(lessonId);
        checkIfCourseAvailable(lesson.getCourse());
        Student student = studentService.findById(studentId);
        checkIfUserRelatedToCourse(lesson, student);
        checkIfRequiredTestPassed(lesson, student);
        Submission submission = new Submission(student, lesson, homeworkEvaluation.getGrade());
        Submission savedSubmission = submissionRepository.save(submission);
        return submissionMapper.toResponseDTO(savedSubmission);
    }

    @Override
    public SubmissionResponseDTO update(LessonEvaluationDTO homeworkEvaluation, Long lessonId, Long studentId) {
        log.info("Updating submission with lesson id: {} and student id: {}", lessonId, studentId);
        Submission submission = findEntityById(lessonId, studentId);
        submission.setGrade(homeworkEvaluation.getGrade());
        return submissionMapper.toResponseDTO(submission);
    }

    @Override
    public void delete(Long lessonId, Long studentId) {
        log.info("Deleting submission with lesson id: {} and student id: {}", lessonId, studentId);
        Submission submission = findEntityById(lessonId, studentId);
        submissionRepository.delete(submission);
    }

    private void checkIfRequiredTestPassed(Lesson lesson, Student student) {
        if (lesson.getTest() != null && lesson.getTest().isAvailable()) {
            TestCompletionStatus testAttempt = testEvaluationService
                    .findAttemptByTestAndStudent(lesson.getTest().getId(), student.getId()).getStatus();
            if (testAttempt != PASSED) {
                throw new SystemException(
                        "Student with id: " + student.getId() + " has not passed the required test", BAD_REQUEST);
            }
        }
    }

    private void checkIfUserRelatedToCourse(Lesson lesson, Student student) {
        checkNull(lesson, student, "Lesson", "Student");
        Long courseId = lesson.getCourse().getId();
        if (!studentRepository.existsByIdAndCoursesId(student.getId(), courseId)) {
            throw new SystemException(
                    "Student with id: " + student.getId() + " is not subscribed to course with id: " + courseId , BAD_REQUEST);
        }
    }

    private void checkIfCourseAvailable(Course course) {
        if (!course.isAvailable()) {
            throw new SystemException("Course is not accessible", BAD_REQUEST);
        }
    }

    private Lesson findLessonById(Long lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow(() ->
                new SystemException("Lesson with id: " + lessonId + " not found.)", NOT_FOUND));
    }

    private Submission findEntityById(Long lessonId, Long studentId) {
        checkNull(lessonId, studentId, "Lesson id", "Student id");
        return submissionRepository.findById_LessonIdAndId_StudentId(lessonId, studentId)
                .orElseThrow(() -> new SystemException(
                        "Submission for user with id: " + studentId + " and lesson with id: " + lessonId + " not found", NOT_FOUND));
    }
}
