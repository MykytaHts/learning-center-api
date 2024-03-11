package com.example.learningcenterapi.mapper;

import com.example.learningcenterapi.domain.Submission;
import com.example.learningcenterapi.dto.request.SubmissionRequestDTO;
import com.example.learningcenterapi.dto.response.SubmissionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface SubmissionMapper extends EntityDTOMapper<SubmissionRequestDTO, SubmissionResponseDTO, Submission> {
    @Override
    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "student.id", target = "studentId")
    SubmissionRequestDTO toRequestDTO(Submission entity);

    @Override
    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "student.id", target = "studentId")
    SubmissionResponseDTO toResponseDTO(Submission entity);

    @Override
    @Mapping(source = "lessonId", target = "id.lessonId")
    @Mapping(source = "lessonId", target = "lesson.id")
    @Mapping(source = "studentId", target = "id.studentId")
    @Mapping(source = "studentId", target = "student.id")
    Submission fromRequestDTO(SubmissionRequestDTO dto);

    @Override
    @Mapping(source = "lessonId", target = "id.lessonId")
    @Mapping(source = "lessonId", target = "lesson.id")
    @Mapping(source = "studentId", target = "id.studentId")
    @Mapping(source = "studentId", target = "student.id")
    Submission fromResponseDTO(SubmissionResponseDTO dto);
}
