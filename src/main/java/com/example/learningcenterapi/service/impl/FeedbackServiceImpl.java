package com.example.learningcenterapi.service.impl;

import com.example.learningcenterapi.domain.Course;
import com.example.learningcenterapi.domain.Feedback;
import com.example.learningcenterapi.domain.Student;
import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.dto.request.FeedbackRequestDTO;
import com.example.learningcenterapi.dto.response.FeedbackResponseDTO;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.mapper.FeedbackMapper;
import com.example.learningcenterapi.repository.CourseRepository;
import com.example.learningcenterapi.repository.FeedbackRepository;
import com.example.learningcenterapi.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final CourseRepository courseRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    public FeedbackResponseDTO findById(Long id) {
        log.info("Finding feedback with id: {}", id);
        return feedbackMapper.toResponseDTO(findEntityById(id));
    }

    @Override
    public List<FeedbackResponseDTO> findAllByCourseId(Long courseId) {
        log.info("Finding feedback by course id: {}", courseId);
        checkNull(courseId, "Course id");
        return feedbackMapper.toResponseDTOList(feedbackRepository.findAllByCourseId(courseId));
    }

    @Override
    public FeedbackResponseDTO save(FeedbackRequestDTO feedbackDTO, User user, Long courseId) {
        log.info("Saving feedback for course with id: {}", courseId);
        Course course = findCourseById(courseId);
        Feedback feedback = new Feedback();
        feedback.setCourse(course);
        feedback.setStudent((Student) user);
        feedback.setFeedback(feedbackDTO.getFeedback());
        feedbackRepository.save(feedback);
        return feedbackMapper.toResponseDTO(feedback);
    }

    @Override
    public FeedbackResponseDTO updateById(FeedbackRequestDTO feedbackDTO, Long feedbackId) {
        log.info("Updating feedback with id: {}", feedbackId);
        Feedback feedback = findEntityById(feedbackId);
        feedback.setFeedback(feedbackDTO.getFeedback());
        return feedbackMapper.toResponseDTO(feedback);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting feedback with id: {}", id);
        Feedback feedback = findEntityById(id);
        feedbackRepository.delete(feedback);
    }

    private Course findCourseById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(
                () -> new SystemException("Course not found with id: " + courseId, NOT_FOUND));
    }

    private Feedback findEntityById(Long id) {
        checkNull(id, "Feedback id");
        return feedbackRepository.findById(id).orElseThrow(
                () -> new SystemException("Feedback not found with id: " + id, NOT_FOUND));
    }
}
