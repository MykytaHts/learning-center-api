package com.example.learningcenterapi.dto.response;

import com.example.learningcenterapi.domain.enumeration.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseStatusDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private CourseStatus courseStatus;
    private Double finalGrade;
}

