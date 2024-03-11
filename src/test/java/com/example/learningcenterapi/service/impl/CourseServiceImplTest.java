package com.example.learningcenterapi.service.impl;

import com.example.learningcenterapi.mapper.CourseMapper;
import com.example.learningcenterapi.repository.CourseRepository;
import com.example.learningcenterapi.repository.StudentRepository;
import com.example.learningcenterapi.repository.SubmissionRepository;
import com.example.learningcenterapi.repository.UserRepository;
import com.example.learningcenterapi.service.InstructorService;
import com.example.learningcenterapi.service.StudentService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@Transactional
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private SubmissionRepository submissionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private InstructorService instructorService;
    @Mock
    private StudentService studentService;
    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

}
