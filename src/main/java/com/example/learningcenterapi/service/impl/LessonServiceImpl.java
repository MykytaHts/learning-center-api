package com.example.learningcenterapi.service.impl;

import com.example.learningcenterapi.domain.Course;
import com.example.learningcenterapi.domain.Lesson;
import com.example.learningcenterapi.dto.request.LessonRequestDTO;
import com.example.learningcenterapi.dto.request.update.CourseUpdateDTO;
import com.example.learningcenterapi.dto.response.LessonResponseDTO;
import com.example.learningcenterapi.dto.response.minimized.LessonMinimizedDTO;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.mapper.LessonMapper;
import com.example.learningcenterapi.repository.CourseRepository;
import com.example.learningcenterapi.repository.LessonRepository;
import com.example.learningcenterapi.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;

    @Override
    @Transactional(readOnly = true)
    public LessonResponseDTO findById(Long lessonId) {
        log.info("Method findById called with id: {}", lessonId);
        return lessonMapper.toResponseDTO(findEntityById(lessonId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonMinimizedDTO> findAllLessonsByCourseId(Long courseId) {
        log.info("Method findAllLessonsByCourseId called with courseId: {}", courseId);
        checkNull(courseId, "Course id");
        return lessonMapper.toMinimizedDTOList(lessonRepository.findAllByCourseId(courseId));
    }

    @Override
    public LessonResponseDTO updateById(LessonRequestDTO lessonDTO, Long lessonId) {
        log.info("Method updateById called with lessonDTO: {} and id: {}", lessonDTO, lessonId);
        Lesson lessonToUpdate = findEntityById(lessonId);
        setUpdatableFields(lessonDTO, lessonId);
        return lessonMapper.toResponseDTO(lessonToUpdate);
    }

    @Override
    public void updateLessonsForCourse(CourseUpdateDTO courseUpdateDTO, Long courseId) {
        log.info("Method updateLessonsForCourse called with courseUpdateDTO: {} and courseId: {}",
                courseUpdateDTO, courseId);
        checkNull(courseId, "Course id");
        Set<LessonRequestDTO> lessons = courseUpdateDTO.getLessons();
        Course course = findCourseById(courseId);
        course.getLessons().clear();
        addLessonsToCourse(lessons, course);
    }

    @Override
    public LessonResponseDTO addToCourse(LessonRequestDTO lessonDTO, Long courseId) {
        log.info("Method addCourse called with lessonDTO: {} and courseId: {}", lessonDTO, courseId);
        checkNull(courseId, "Course id");
        if (lessonDTO.getId() != null) {
            throw new SystemException("Lesson id must be null", BAD_REQUEST);
        }
        Lesson lesson = lessonMapper.fromRequestDTO(lessonDTO);
        Course course = findCourseById(courseId);
        course.addLesson(lesson);
        return lessonMapper.toResponseDTO(lessonRepository.save(lesson));
    }

    @Override
    public void deleteById(Long lessonId) {
        log.info("Method deleteById called with id: {}", lessonId);
        Lesson lesson = findEntityById(lessonId);
        lesson.getCourse().removeLesson(lesson);
    }

    private void addLessonsToCourse(Set<LessonRequestDTO> lessons, Course course) {
        for (LessonRequestDTO lessonDTO : lessons) {
            if (Objects.isNull(lessonDTO.getId())) {
                Lesson lesson = lessonMapper.fromRequestDTO(lessonDTO);
                course.addLesson(lesson);
            } else {
                Lesson lessonToUpdate = findEntityById(lessonDTO.getId());
                setUpdatableFields(lessonDTO, lessonDTO.getId());
                course.addLesson(lessonToUpdate);
            }
        }
    }

    private void setUpdatableFields(LessonRequestDTO lessonDTO, Long lessonId) {
        Lesson existingLesson = findEntityById(lessonId);
        existingLesson.setTitle(lessonDTO.getTitle());
        existingLesson.setContent(lessonDTO.getContent());
        existingLesson.setOrderIndex(lessonDTO.getOrderIndex());
    }

    private Course findCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new SystemException("Course with id: " + courseId + " not found", NOT_FOUND));
    }

    private Lesson findEntityById(Long lessonId) {
        checkNull(lessonId, "Lesson id");
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new SystemException("Lesson with id: " + lessonId + " not found", NOT_FOUND));
    }
}
