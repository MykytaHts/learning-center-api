package com.example.learningcenterapi.mapper;

import com.example.learningcenterapi.domain.TestAttempt;
import com.example.learningcenterapi.dto.response.TestAttemptDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface TestAttemptMapper {

    @Mapping(source = "test.id", target = "testId")
    TestAttemptDTO toDTO(TestAttempt testAttempt);

    @Mapping(source = "testId", target = "id.testId")
    @Mapping(source = "testId", target = "test.id")
    TestAttempt toEntity(TestAttemptDTO testAttemptDTO);
}
