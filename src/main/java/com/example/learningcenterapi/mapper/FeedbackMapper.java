package com.example.learningcenterapi.mapper;

import com.example.learningcenterapi.domain.Feedback;
import com.example.learningcenterapi.dto.request.FeedbackRequestDTO;
import com.example.learningcenterapi.dto.response.FeedbackResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface FeedbackMapper extends EntityDTOMapper<FeedbackRequestDTO, FeedbackResponseDTO, Feedback> {
    @Override
    @Mapping(source = "student.id", target = "studentId")
    FeedbackResponseDTO toResponseDTO(Feedback feedback);
}
