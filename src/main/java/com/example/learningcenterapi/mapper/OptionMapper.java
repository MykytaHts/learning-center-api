package com.example.learningcenterapi.mapper;

import com.example.learningcenterapi.domain.Option;
import com.example.learningcenterapi.dto.request.OptionRequestDTO;
import com.example.learningcenterapi.dto.response.OptionResponseDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OptionMapper extends EntityDTOMapper<OptionRequestDTO, OptionResponseDTO, Option>{
}
