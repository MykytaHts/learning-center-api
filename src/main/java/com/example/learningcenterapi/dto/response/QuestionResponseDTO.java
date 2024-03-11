package com.example.learningcenterapi.dto.response;

import com.example.learningcenterapi.domain.enumeration.QuestionComplexity;
import com.example.learningcenterapi.domain.enumeration.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.Set;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QuestionResponseDTO extends BaseResponseDTO {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long id;
    private String description;
    private Integer orderIndex;
    private QuestionType questionType;
    private QuestionComplexity questionComplexity;
    private Set<OptionResponseDTO> options;
}
