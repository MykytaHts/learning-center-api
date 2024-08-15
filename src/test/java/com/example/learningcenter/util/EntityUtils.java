package com.example.learningcenter.util;

import com.example.learningcenterapi.domain.Course;
import com.example.learningcenterapi.domain.Feedback;
import com.example.learningcenterapi.domain.Student;
import com.example.learningcenterapi.domain.enumeration.Role;

public class EntityUtils {
    public static Student generateStudent() {
        Student student = new Student();
        student.setEmail("student@example.com");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setRole(Role.STUDENT);
        return student;
    }

    public static Course generateCourse() {
        Course course = new Course();
        course.setTitle("Test Course");
        course.setDescription("Test Course Description");
        return course;
    }

    public static Feedback generateFeedback() {
        Feedback feedback = new Feedback();
        feedback.setFeedback("What a nice feedback");
        return feedback;
    }
}
