package com.example.learningcenterapi.mapper;

import com.example.learningcenterapi.domain.Lesson;
import com.example.learningcenterapi.dto.request.LessonRequestDTO;
import com.example.learningcenterapi.dto.response.LessonResponseDTO;
import com.example.learningcenterapi.dto.response.minimized.LessonMinimizedDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface LessonMapper extends EntityDTOMapper<LessonRequestDTO, LessonResponseDTO, Lesson> {

    @Override
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "test.id", target = "testId")
    LessonResponseDTO toResponseDTO(Lesson entity);

    @Override
    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "testId", target = "test.id")
    Lesson fromResponseDTO(LessonResponseDTO dto);

    List<LessonMinimizedDTO> toMinimizedDTOList(Collection<Lesson> entities);

    LessonMinimizedDTO toMinimizedDTO(Lesson entity);
}
