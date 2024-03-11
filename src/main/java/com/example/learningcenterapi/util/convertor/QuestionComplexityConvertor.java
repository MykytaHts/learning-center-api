package com.example.learningcenterapi.util.convertor;

import com.example.learningcenterapi.domain.enumeration.QuestionComplexity;
import jakarta.persistence.AttributeConverter;

public class QuestionComplexityConvertor implements AttributeConverter<QuestionComplexity, Integer> {
    @Override
    public QuestionComplexity convertToEntityAttribute(Integer value) {
        return QuestionComplexity.valueOf(value);
    }

    @Override
    public Integer convertToDatabaseColumn(QuestionComplexity value) {
        return value.getIndex();
    }
}
