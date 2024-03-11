package com.example.learningcenterapi.service.impl;

import com.example.learningcenterapi.domain.Student;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.repository.StudentRepository;
import com.example.learningcenterapi.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    @Transactional(readOnly = true)
    public Student findById(Long studentId) {
        log.info("Finding student with id: {}", studentId);
        return studentRepository.findById(studentId).orElseThrow(
                () -> new SystemException("Student not found with id: " + studentId + "", NOT_FOUND));
    }
}
