package com.example.learningcenterapi.mapper;

import com.example.learningcenterapi.domain.Homework;
import com.example.learningcenterapi.dto.request.HomeworkRequestDTO;
import com.example.learningcenterapi.dto.response.HomeworkResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface HomeworkMapper extends EntityDTOMapper<HomeworkRequestDTO, HomeworkResponseDTO, Homework> {

    @Override
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "lesson.id", target = "lessonId")
    HomeworkResponseDTO toResponseDTO(Homework entity);

    @Override
    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "lessonId", target = "lesson.id")
    Homework fromResponseDTO(HomeworkResponseDTO dto);
}
