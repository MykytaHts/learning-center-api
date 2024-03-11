package com.example.learningcenterapi.repository;

import com.example.learningcenterapi.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    List<Course> findAllByStudentsId(Long studentId);

    List<Course> findAllByInstructorsId(Long instructorId);

    boolean existsByIdAndStudentsId(Long courseId, Long studentId);

    boolean existsByLessonsIdAndStudentsId(Long lessonId, Long studentId);

    boolean existsByIdAndInstructorsId(Long courseId, Long instructorId);

    boolean existsByLessonsIdAndInstructorsId(Long lessonId, Long instructorId);

    boolean existsByTestsIdAndAndStudentsId(Long testId, Long studentId);


    boolean existsByTestsIdAndInstructorsId(Long testId, Long instructorId);
}
