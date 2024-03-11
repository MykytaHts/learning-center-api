package com.example.learningcenterapi.service.impl;


import com.example.learningcenterapi.domain.*;
import com.example.learningcenterapi.dto.response.CourseStatusDTO;
import com.example.learningcenterapi.dto.request.CourseRequestDTO;
import com.example.learningcenterapi.dto.response.CourseResponseDTO;
import com.example.learningcenterapi.dto.response.minimized.CourseMinimizedDTO;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.mapper.CourseMapper;
import com.example.learningcenterapi.repository.CourseRepository;
import com.example.learningcenterapi.repository.StudentRepository;
import com.example.learningcenterapi.repository.SubmissionRepository;
import com.example.learningcenterapi.repository.UserRepository;
import com.example.learningcenterapi.service.CourseService;
import com.example.learningcenterapi.service.InstructorService;
import com.example.learningcenterapi.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.example.learningcenterapi.domain.enumeration.CourseStatus.*;
import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CourseServiceImpl implements CourseService {
    private final static int MIN_SUCCESS_GRADE = 80;

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final InstructorService instructorService;
    private final StudentService studentService;
    private final CourseMapper courseMapper;

    @Override
    @Transactional(readOnly = true)
    public CourseResponseDTO findById(Long courseId) {
        log.info("Finding course with id: {}", courseId);
        Course course = findEntityById(courseId);
        return courseMapper.toResponseDTO(course);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseMinimizedDTO> findAll(Pageable pageable) {
        log.info("Finding all courses");
        Page<Course> courses = courseRepository.findAll(pageable);
        return courses.map(courseMapper::toMinimizedDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseMinimizedDTO> findAllByStudentId(Long studentId) {
        log.info("Finding all courses by student id: {}", studentId);
        checkNull(studentId, "Student id");
        List<Course> courses = courseRepository.findAllByStudentsId(studentId);
        return courseMapper.toMinimizedDTOList(courses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseMinimizedDTO> findAllByInstructorId(Long instructorId) {
        log.info("Finding all courses by instructor id: {}", instructorId);
        checkNull(instructorId, "Instructor id");
        List<Course> courses = courseRepository.findAllByInstructorsId(instructorId);
        return courseMapper.toMinimizedDTOList(courses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseMinimizedDTO> findMyCourses(Long userId) {
        log.info("Finding all courses by user id: {}", userId);
        courseRepository.findAllByInstructorsId(userId);
        User user = findTestById(userId);

        if (user instanceof Instructor instructor) {
            List<Course> courses = courseRepository.findAllByInstructorsId(instructor.getId());
            return courseMapper.toMinimizedDTOList(courses);
        } else if (user instanceof Student student) {
            List<Course> courses = courseRepository.findAllByStudentsId(student.getId());
            return courseMapper.toMinimizedDTOList(courses);
        }

        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseStatusDTO getUserCourseStatus(Long courseId, Long studentId) {
        log.info("Getting course status for course with id: {} and student with id: {}", courseId, studentId);
        checkIfStudentRelatedToCourse(studentId, courseId);
        Course course = findEntityById(courseId);
        checkIfCourseAvailable(course);
        List<Submission> submissions = submissionRepository
                .findAllByStudentIdAndLessonCourseId(studentId, course.getId());
        CourseStatusDTO courseGradeDTO = new CourseStatusDTO();
        if (course.getLessons().size() > submissions.size()) {
            log.info("Course Status: In Progress! Course id: {}, Student id: {}", courseId, studentId);
            courseGradeDTO.setCourseStatus(IN_PROGRESS);
        } else {
            calculateFinalGrade(courseGradeDTO, course, submissions);
        }
        return courseGradeDTO;
    }

    @Override
    public CourseResponseDTO save(CourseRequestDTO courseDTO, Long userId) {
        log.info("Saving course: {}", courseDTO);
        Course course = courseMapper.fromRequestDTO(courseDTO);
        course = courseRepository.save(course);
        User user = findTestById(userId);
        if (user instanceof Instructor instructor) {
            course.addInstructor(instructor);
        }
        return courseMapper.toResponseDTO(course);
    }

    @Override
    public CourseResponseDTO updateById(CourseRequestDTO courseDTO, Long courseId) {
        log.info("Updating course: {}", courseDTO);
        Course course = findEntityById(courseId);
        setUpdatableFields(course, courseDTO);
        return courseMapper.toResponseDTO(course);
    }

    @Override
    public void addStudentToCourse(Long courseId, Long userId) {
        log.info("Trying to add student: {} to course: {}", userId, courseId);
        checkNull(courseId, userId, "Course id", "User id");
        User user = studentService.findById(userId);
        Course course = findEntityById(courseId);
        checkIfCourseAvailable(course);
        if (user instanceof Student student) {
            student.addCourse(course);
            log.info("User is a student, Successfully added student: {} to course: {}", userId, courseId);
        } else {
            log.error("User is not a student, Failed to add student: {} to course: {}", userId, courseId);
        }
    }

    @Override
    public void addInstructorToCourse(Long courseId, Long userId) {
        log.info("Trying to add instructor: {} to course: {}", userId, courseId);
        checkNull(courseId, userId, "Course id", "User id");
        User user = instructorService.findById(userId);
        Course course = findEntityById(courseId);
        if (user instanceof Instructor instructor) {
            instructor.addCourse(course);
            log.info("User is an instructor, Successfully added instructor: {} to course: {}", userId, courseId);
        } else {
            log.error("User is not an instructor, Failed to add instructor: {} to course: {}", userId, courseId);
        }
    }

    @Override
    public void deleteById(Long courseId) {
        log.info("Deleting course with id: {}", courseId);
        Course course = findEntityById(courseId);
        courseRepository.delete(course);
    }

    @Override
    public void deleteInstructorFromCourse(Long courseId, Long instructorId) {
        checkNull(courseId, instructorId, "Course id", "Instructor id");
        log.info("Trying to delete instructor: {} from course: {}", instructorId, courseId);
        Instructor instructor = instructorService.findById(instructorId);
        Course course = findEntityById(courseId);
        instructor.removeCourse(course);
        log.info("Successfully deleted instructor: {} from course: {}", instructorId, courseId);
    }

    private void calculateFinalGrade(CourseStatusDTO courseGradeDTO, Course course, List<Submission> submissions) {
        double finalGrade = submissions.stream().mapToDouble(Submission::getGrade).sum() / submissions.size();
        courseGradeDTO.setFinalGrade(finalGrade);

        if (finalGrade >= MIN_SUCCESS_GRADE) {
            log.info("Course Status: Completed Successfully! Final grade: {}\n Course id: {}, Student id: {}",
                    finalGrade, course.getId(), course.getId());
            courseGradeDTO.setCourseStatus(COMPLETED);
        } else {
            log.info("Course Status: Failed! Grade level wasn't passed. Final grade: {}\n Course id: {}, Student id: {}",
                    finalGrade, course.getId(), course.getId());
            courseGradeDTO.setCourseStatus(FAILED);
        }
    }

    private void checkIfCourseAvailable(Course course) {
        if (!course.isAvailable()) {
            throw new SystemException("Course is not accessible", BAD_REQUEST);
        }
    }

    private void checkIfStudentRelatedToCourse(Long studentId, Long courseId) {
        if (!studentRepository.existsByIdAndCoursesId(studentId, courseId)) {
            throw new SystemException("Student is not related to the course with id:" + courseId, BAD_REQUEST);
        }
    }

    private void setUpdatableFields(Course course, CourseRequestDTO courseDTO) {
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
    }

    private User findTestById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new SystemException("User not found with id: " + userId, NOT_FOUND));
    }

    private Course findEntityById(Long courseId) {
        checkNull(courseId, "Course id");
        return courseRepository.findById(courseId).orElseThrow(
                () -> new SystemException("Course not found with id: " + courseId + "", NOT_FOUND));
    }
}