package com.example.learningcenter.configuration;

import com.example.learningcenter.annotations.MockStudent;
import com.example.learningcenterapi.domain.Student;
import com.example.learningcenterapi.domain.enumeration.Role;
import com.example.learningcenterapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomMockStudentSecurityContextFactory implements WithSecurityContextFactory<MockStudent> {
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public SecurityContext createSecurityContext(MockStudent customMockStudent) {
        Student student = createStudent();
        student = studentRepository.save(student);
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(student, null, student.getAuthorities());
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        SecurityContextHolder.setContext(securityContext);
        TestSecurityContextHolder.setContext(securityContext);
        return securityContext;
    }

    private static Student createStudent() {
        Student student = new Student();
        student.setFirstName("Super");
        student.setLastName("Student");
        student.setEmail("super.student@gmail.com");
        student.setPassword("$2a$12$icb9BIES3BgjXkHv1V2acu4YPcYJGNUVwjg2gZtyuSDVO4bQ");
        student.setRole(Role.STUDENT);
        return student;
    }
}
