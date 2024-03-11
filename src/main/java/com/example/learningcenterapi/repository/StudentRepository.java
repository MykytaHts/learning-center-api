package com.example.learningcenterapi.repository;

import com.example.learningcenterapi.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    boolean existsByIdAndCoursesId(Long student, Long courseId);

    List<Student> findAllByCoursesId(Long courseId);
}
