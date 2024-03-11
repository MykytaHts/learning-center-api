package com.example.learningcenterapi.service.impl;

import com.example.learningcenterapi.domain.Lesson;
import com.example.learningcenterapi.domain.Test;
import com.example.learningcenterapi.dto.request.TestRequestDTO;
import com.example.learningcenterapi.dto.response.TestResponseDTO;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.mapper.TestMapper;
import com.example.learningcenterapi.repository.LessonRepository;
import com.example.learningcenterapi.repository.TestRepository;
import com.example.learningcenterapi.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final LessonRepository lessonRepository;
    private final TestMapper testMapper;

    @Override
    @Transactional(readOnly = true)
    public TestResponseDTO findById(Long testId) {
        log.info("Finding test by id: {}", testId);
        return testMapper.toResponseDTO(findEntityById(testId));
    }

    @Override
    public TestResponseDTO assignToLesson(TestRequestDTO testDTO, Long lessonId) {
        log.info("Saving test: {}", testDTO);
        Lesson lesson = findLessonById(lessonId);
        checkIfCanAddTestToLesson(lesson);
        Test test = testMapper.fromRequestDTO(testDTO);
        test.setCourse(lesson.getCourse());
        lesson.setTest(test);
        return testMapper.toResponseDTO(testRepository.save(test));
    }

    @Override
    public TestResponseDTO updateById(TestRequestDTO testDTO, Long testId) {
        log.info("Updating test by id: {}", testId);
        Test test = findEntityById(testId);
        test.setTitle(testDTO.getTitle());
        test.setDescription(testDTO.getDescription());
        return testMapper.toResponseDTO(test);
    }

    @Override
    public void deleteById(Long testId) {
        log.info("Deleting test by id: {}", testId);
        Test test = findEntityById(testId);
        testRepository.delete(test);
    }

    private void checkIfCanAddTestToLesson(Lesson lesson) {
        if (lesson.getTest() != null) {
            throw new SystemException("This lesson already has the test.", CONFLICT);
        }
    }

    private Lesson findLessonById(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new SystemException("Lesson with id: " + lessonId + " not found", NOT_FOUND));
    }

    private Test findEntityById(Long testId) {
        checkNull(testId, "Test id");
        return testRepository.findById(testId)
                .orElseThrow(() -> new SystemException("Test with id: " + testId + " not found", NOT_FOUND));
    }
}