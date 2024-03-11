package com.example.learningcenterapi.mapper;

import com.example.learningcenterapi.domain.Question;
import com.example.learningcenterapi.dto.request.QuestionRequestDTO;
import com.example.learningcenterapi.dto.response.QuestionResponseDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = OptionMapper.class)
public interface QuestionMapper extends EntityDTOMapper<QuestionRequestDTO, QuestionResponseDTO, Question> {
}
