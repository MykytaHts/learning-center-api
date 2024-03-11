package com.example.learningcenterapi.util.convertor;

import com.example.learningcenterapi.domain.enumeration.QuestionType;
import jakarta.persistence.AttributeConverter;

public class QuestionTypeConvertor implements AttributeConverter<QuestionType, Integer> {
    @Override
    public QuestionType convertToEntityAttribute(Integer value) {
        return QuestionType.valueOf(value);
    }

    @Override
    public Integer convertToDatabaseColumn(QuestionType value) {
        return value.getIndex();
    }
}
