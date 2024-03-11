package com.example.learningcenterapi.service.impl;

import com.example.learningcenterapi.domain.Instructor;
import com.example.learningcenterapi.exception.SystemException;
import com.example.learningcenterapi.repository.InstructorRepository;
import com.example.learningcenterapi.service.InstructorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class InstructorServiceImpl implements InstructorService {
    private final InstructorRepository instructorRepository;

    @Override
    @Transactional(readOnly = true)
    public Instructor findById(Long instructorId) {
        log.info("Finding instructor with id: {}", instructorId);
        return instructorRepository.findById(instructorId).orElseThrow(
                () -> new SystemException("Instructor not found with id: " + instructorId + "", NOT_FOUND));
    }
}
