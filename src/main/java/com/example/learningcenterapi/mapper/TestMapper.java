package com.example.learningcenterapi.mapper;

import com.example.learningcenterapi.domain.Test;
import com.example.learningcenterapi.dto.request.TestRequestDTO;
import com.example.learningcenterapi.dto.response.TestResponseDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {LessonMapper.class})
public interface TestMapper extends EntityDTOMapper<TestRequestDTO, TestResponseDTO, Test> {
}
