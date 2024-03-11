package com.example.learningcenterapi.mapper;

import com.example.learningcenterapi.domain.Instructor;
import com.example.learningcenterapi.dto.request.UserRequestDTO;
import com.example.learningcenterapi.dto.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface InstructorMapper extends EntityDTOMapper<UserRequestDTO, UserResponseDTO, Instructor> {
}

