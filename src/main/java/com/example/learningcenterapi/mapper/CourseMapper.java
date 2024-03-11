package com.example.learningcenterapi.mapper;

import com.example.learningcenterapi.domain.Course;
import com.example.learningcenterapi.dto.request.CourseRequestDTO;
import com.example.learningcenterapi.dto.response.CourseResponseDTO;
import com.example.learningcenterapi.dto.response.minimized.CourseMinimizedDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = LessonMapper.class)
public interface CourseMapper extends EntityDTOMapper<CourseRequestDTO, CourseResponseDTO, Course> {
    List<CourseMinimizedDTO> toMinimizedDTOList(List<Course> courses);
    CourseMinimizedDTO toMinimizedDTO(Course course);
}
