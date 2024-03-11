package com.example.learningcenterapi.service;

import com.example.learningcenterapi.domain.User;
import com.example.learningcenterapi.dto.request.FeedbackRequestDTO;
import com.example.learningcenterapi.dto.response.FeedbackResponseDTO;

import java.util.List;

public interface FeedbackService {
    FeedbackResponseDTO findById(Long id);

    List<FeedbackResponseDTO> findAllByCourseId(Long courseId);

    FeedbackResponseDTO save(FeedbackRequestDTO feedbackDTO, User id, Long courseId);

    FeedbackResponseDTO updateById(FeedbackRequestDTO feedbackDTO, Long feedbackId);

    void deleteById(Long id);
}
